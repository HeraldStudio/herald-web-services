<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <link href="css/bootstrap.min.css" rel="stylesheet" media="screen"/>
                <link href="css/style.css" rel="stylesheet" media="screen"/>
                <script src="js/jquery-2.0.1.min.js"></script>
                <script src="js/bootstrap.min.js"></script>
                <script src="js/loadSchedule.js"></script>
            </head>
            <body>
                <script type="text/javascript">
                <![CDATA[
                    $(document).ready(function() {
                    $("#selectedDay").change(function () {
                    loadSchedule($("#selectedDay").val());
                    });
                    $("#selectedDay").get(0).selectedIndex = new Date().getDay();
                    loadSchedule($("#selectedDay").val());
                    });
                ]]>
                </script>
                <div id="curriculum" class="container">
                    <div class="row">
                        <select id="selectedDay" class="pull-right">
                            <option value="SUN">周日</option>
                            <option value="MON">周一</option>
                            <option value="TUE">周二</option>
                            <option value="WED">周三</option>
                            <option value="THU">周四</option>
                            <option value="FRI">周五</option>
                            <option value="SAT">周六</option>
                        </select>
                    </div>
                    <div class="row">
                        <div class="span5">
                            <h4>课程</h4>
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>课程</th>
                                        <th>教师</th>
                                        <th style="width: 40px">学分</th>
                                        <th style="width: 40px">周次</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <xsl:for-each select="courses/course">
                                    <tr>
                                        <td><a href="{@href}"><xsl:value-of select="name"/></a></td>
                                        <td><xsl:value-of select="lecturer"/></td>
                                        <td><xsl:value-of select="credit"/></td>
                                        <td><xsl:value-of select="week/@from"/>-<xsl:value-of select="week/@to"/></td>
                                    </tr>
                                    </xsl:for-each>
                                </tbody>
                            </table>
                        </div>
                        <div class="span7">
                            <h4>安排</h4>
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>课程</th>
                                        <th>节次</th>
                                        <th>地点</th>
                                        <th>单双周</th>
                                    </tr>
                                </thead>
                                <xsl:for-each select="timeTable/schedule">
                                <tbody id="{@day}" class="schedule">
                                    <xsl:for-each select="attendance">
                                    <tr>
                                        <td><xsl:value-of select="@courseName"/></td>
                                        <td><xsl:value-of select="period/@from"/>-<xsl:value-of select="period/@to"/></td>
                                        <td><xsl:value-of select="@place"/></td>
                                        <td><xsl:value-of select="@strategy"/></td>
                                    </tr>
                                    </xsl:for-each>
                                </tbody>
                                </xsl:for-each>
                            </table>
                        </div>

                    </div>
                </div>
            </body>

        </html>
    </xsl:template>
</xsl:stylesheet>