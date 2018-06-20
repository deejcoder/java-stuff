<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html> 
<body>
  <h2>House of Representatives</h2>
  <table border="1">
    <tr bgcolor="#CDF119">
      <th style="text-align:left">Name</th>
      <th style="text-align:left">State</th>
      <th style="text-align:left">Phone</th>
    </tr>
    <xsl:for-each select="MemberData/members/member/member-info">
    <xsl:if test="party='D'">
      <tr style='color:blue'>
        <td><xsl:value-of select="namelist"/></td>
        <td><xsl:value-of select="state"/></td>
        <td><xsl:value-of select="phone"/></td>
      </tr>
    </xsl:if>
    <xsl:if test="party='R'">
      <tr style='color:red'>
        <td><xsl:value-of select="namelist"/></td>
        <td><xsl:value-of select="state"/></td>
        <td><xsl:value-of select="phone"/></td>
      </tr>
    </xsl:if>
    </xsl:for-each>
  </table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
