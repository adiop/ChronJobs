<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>ECN Summary Notification System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body style="margin: 50px; padding: 50px; background-color: #fcfcfc;">

    <style>

        .header {
            background-color: rgb(230, 235, 239);
            font-family: Arial, sans-serif;
            font-size: 16px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
            margin-top: 10px;
            border-style: solid;
            border-width: thin;
        }

        .headerEcn {
            font-family: Arial, sans-serif;
            font-size: 14px;
            text-align: left;
            margin-left: 10px;
            margin-bottom: 10px;
            margin-top: 10px;
            border-style: solid;
            border-width: thin;
        }

        .headerPart {
            font-family: Arial, sans-serif;
            font-size: 12px;
            text-align: left;
            margin-left: 10px;
            margin-bottom: 10px;
            margin-top: 10px;
            border-style: solid;
            border-width: thin;
        }

        .bodyPart {
            font-family: Arial, sans-serif;
            font-size: 12px;
            text-align: left;
            margin-left: 10px;
            margin-bottom: 10px;
            margin-top: 10px;
            border-style: solid;
            border-width: thin;
        }

        .blank {
            background-color: #FFF;
            border-style: none;
            border-width: 0px;
            padding-top: 20px;
        }

    </style>

    <table width="100%" cellspacing="0" cellpadding="0">
        <tbody>
            <tr>
                <td class="header" colspan="2">
                    Items Assigned to you
                </td>
            </tr>
            <#assign ecnList = ecnSummary.ecns >
            <#list ecnList as ecn >
                <tr>
                    <td width="100%" class="blank" colspan="2">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td class="headerEcn" width="20%">
                        <b>Ecn Number</b> : ${ecn.ecnNumber}
                    </td>
                    <td class="headerEcn" width="80%">
                        <b>Reason Code - Change Description</b> : ${ecn.changeDescription}
                    </td>
                </tr>
                <tr>
                    <td width="100%" colspan="2">
                        <table width="100%" cellspacing="0" cellpadding="0">
                            <thead>
                                <tr>
                                    <th class="headerPart" width="20%">Item Number</th>
                                    <th class="headerPart" width="25%">Revision</th>
                                    <th class="headerPart" width="25%">Status</th>
                                    <th class="headerPart" width="30%">Days Assigned</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#assign ecnPartsList = ecn.ecnParts >
                                <#list ecnPartsList as part >
                                    <tr style="background-color: ${part.ecnStatusColor}">
                                        <td class="bodyPart">${part.partNumber}</td>
                                        <td class="bodyPart">${part.revNumber}</td>
                                        <td class="bodyPart">${part.ecnStatusText}</td>
                                        <td class="bodyPart">${part.daysAssigned}</td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                        <br/>
                        <br/>
                    </td>
                </tr>
            </#list>

        </tbody>
    </table>

<div style="font-family: Arial, sans-serif; margin-top: 20px;">
    &nbsp;Thanks,<br/>
    &nbsp;<img src="data:image/png;base64,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" width="200" height="140"><br/>
    &nbsp;ECN Summary Notification System<br/>
    &nbsp;Warn Industries Inc. | 12900 SE Capps Rd. | Clackamas, OR<br/>
    &nbsp;97015<br/>
    &nbsp;E-mail: helpdesk@warn.com<br/>
</div>

</body>
</html>