package com.warn.chron.chronjobs.job.pdm.masterlog.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.pdm.masterlog.entities.Drawing;
import com.warn.chron.chronjobs.persistence.entity.Notification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MasterlogDrawings {
    private static final String QUERY = "SELECT TOP 100 * FROM %DB_PDM%.dbo.drawing_pdf_location WHERE file_data IS NULL";

    private static final String UPDATE_QUERY = "UPDATE %DB_PDM%.dbo.drawing_pdf_location" +
            " SET file_data = ?  " +
            " WHERE item_number = ? AND file_name = ?";

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate pdmJdbcTemplate;

//    @Scheduled(fixedRate = 1000000)
//    @Scheduled(cron = "0 0/20 * * * ?")
    public void execute() {
        String query = QUERY
                .replaceAll("%DB_PDM%", dbApplicationConfig.getPdm().getDb());

        String update_query = UPDATE_QUERY
                .replaceAll("%DB_PDM%", dbApplicationConfig.getPdm().getDb());

        System.out.println(query);

        List<Drawing> drawingList = pdmJdbcTemplate.query(
                query,
                (rs, rowNum) ->
                        new Drawing(
                                rs.getString("item_number"),
                                rs.getString("item_revision"),
                                rs.getString("drawing_version"),
                                rs.getString("path_to_drawing"),
                                rs.getString("unc_server"),
                                rs.getString("file_name"),
                                rs.getDate("file_datetime"),
                                rs.getInt("file_index"),
                                rs.getBytes("file_data")
                        )
        );

        System.out.println("There are " + drawingList.size() + " Items in list!!!");

        int updateCount[] = batchUpdate(drawingList,update_query);

    }

    public int[] batchUpdate(final List<Drawing> drawings, String update_query) {
        int [] updateCounts = pdmJdbcTemplate.batchUpdate(
                update_query,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setBytes(1, getFileBytes(drawings.get(i).getPathToDrawing()));
                        ps.setString(2,drawings.get(i).getPartNumber());
                        ps.setString(3,drawings.get(i).getFileName());
                    }

                    @Override
                    public int getBatchSize() {
                        return drawings.size();
                    }
                }
        );

        return updateCounts;
    }

    public byte[] getFileBytes(String pathToDrawing) {
        try {
            return Files.readAllBytes(Paths.get(pathToDrawing));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
