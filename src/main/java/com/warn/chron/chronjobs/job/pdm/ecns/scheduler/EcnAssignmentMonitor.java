package com.warn.chron.chronjobs.job.pdm.ecns.scheduler;

import com.warn.chron.chronjobs.application.common.ApplicationEnvironmentConfig;
import com.warn.chron.chronjobs.job.pdm.ecns.business.EcnNotificationService;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.Ecn;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.EcnAssignment;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.EcnPart;
import com.warn.chron.chronjobs.job.pdm.ecns.entities.EcnSummary;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EcnAssignmentMonitor {

    private static final String QUERY = "SELECT %DB_INTRANET%.dbo.employees.FullName, ea.approver, ea.resolution, ea.ecn_number, ea.IMLITM, ea.IMRVNO,"
            + " ecn_status.status_text, %DB_INTRANET%.dbo.employees.email, isnull(ecns.project_comment, '') AS project_comment,"
            + " DATEDIFF(d, ea.assigned_time, GETDATE()) AS days_assigned,"
            + " ea.assigned_time, ecn_status_color, reason_code, ecns.change_description"
            + " FROM %DB_PDM%.dbo.ecn_approvals ea"
            + " INNER JOIN %DB_PDM%.dbo.ecns ON ea.ecn_number = ecns.ecn_number"
            + " INNER JOIN %DB_PDM%.dbo.master_log ON ea.ecn_number = master_log.ecn_number AND ea.IMLITM = master_log.IMLITM AND ea.IMRVNO = master_log.IMRVNO"
            + " INNER JOIN %DB_PDM%.dbo.ecn_status ON master_log.ecn_status = ecn_status.status_code"
            + " INNER JOIN %DB_INTRANET%.dbo.employees ON ea.approver = %DB_INTRANET%.dbo.employees.login"
            + " WHERE     (ea.resolution = 3) ORDER BY %DB_INTRANET%.dbo.employees.FullName, CASE DATEDIFF(d, ea.assigned_time, GETDATE()) WHEN 1 THEN 1 WHEN 2 THEN 1 WHEN 3 THEN 1 ELSE ea.ecn_number END, ea.IMLITM";

    @NonNull
    private ApplicationEnvironmentConfig dbApplicationConfig;

    @NonNull
    private JdbcTemplate intranetJdbcTemplate;

    @Autowired
    private EcnNotificationService ecnNotificationService;

//    @Scheduled(fixedRate = 1000000)
//    @Scheduled(cron = "0 40 5 * * ?")
    public void execute() {
        String query = QUERY
                .replaceAll("%DB_INTRANET%",dbApplicationConfig.getIntranet().getDb())
                .replaceAll("%DB_PDM%",dbApplicationConfig.getPdm().getDb());

        System.out.println(query);

        List<EcnAssignment> ecnAssignmentList = intranetJdbcTemplate.query(
                    query,
                    (rs, rowNum) ->
                        new EcnAssignment(
                                rs.getString("FullName"),
                                rs.getString("approver"),
                                rs.getInt("resolution"),
                                rs.getString("ecn_number"),
                                rs.getString("IMLITM"),
                                rs.getString("IMRVNO"),
                                rs.getString("status_text"),
                                rs.getString("email"),
                                rs.getString("project_comment"),
                                rs.getInt("days_assigned"),
                                rs.getDate("assigned_time"),
                                rs.getString("ecn_status_color"),
                                rs.getString("reason_code"),
                                rs.getString("change_description")
                        )
        );

        Map<String, List<EcnAssignment>> ecnAssignmentMap = ecnAssignmentList
                .stream()
                .collect(Collectors.groupingBy(EcnAssignment::getApprover));

        ecnAssignmentMap.keySet()
                .parallelStream()
                .forEach(approver -> {
                    List<EcnAssignment> ecnAssignmentApproverList = ecnAssignmentMap.get(approver);
                    getAssignedSummaryForTheApprover(ecnAssignmentApproverList);
                });

    }

    public void getAssignedSummaryForTheApprover (List<EcnAssignment> assignmentList) {

        EcnAssignment assignment = assignmentList.get(0);

        EcnSummary ecnSummary = new EcnSummary();
        ecnSummary.setApprover(assignment.getApprover());
        ecnSummary.setEmail(assignment.getEmail());
        ecnSummary.setEcns(createEcnListForApprover(assignmentList));

        ecnNotificationService.sendEcnSummary(ecnSummary);

    }

    public List<Ecn> createEcnListForApprover (List<EcnAssignment> ecnAssignmentList) {

        Map<String, List<EcnAssignment>> ecnPartMap = ecnAssignmentList.stream().collect(Collectors.groupingBy(EcnAssignment::getEcnNumber));

        List<Ecn> ecnList = ecnPartMap.keySet()
                .stream()
                .map(ecnNumber -> {

                    EcnAssignment assignment = ecnPartMap.get(ecnNumber).get(0);

                    Ecn ecn = new Ecn();
                    ecn.setEcnNumber(assignment.getEcnNumber());
                    ecn.setChangeDescription(assignment.getChangeDescription());

                    ecn.setEcnParts(ecnPartMap.get(ecnNumber)
                            .stream()
                            .map(ecnAssignment -> {

                                EcnPart ecnPart = new EcnPart();
                                ecnPart.setPartNumber(ecnAssignment.getPartNumber());
                                ecnPart.setRevNumber(ecnAssignment.getRevNumber());
                                ecnPart.setDaysAssigned(ecnAssignment.getDaysAssigned());
                                ecnPart.setEcnStatusText(ecnAssignment.getEcnStatusText());
                                ecnPart.setEcnStatusColor(ecnAssignment.getEcnStatusColor());

                                return ecnPart;

                            })
                            .collect(Collectors.toList()));

                    return ecn;

                })
                .collect(Collectors.toList());

        return ecnList;
    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
