<?xml version="1.0"?>

<xsl:stylesheet version="1.0"

xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/workflow">
<html>
            <head>
                <title>
                    workflow
               </title>
            </head>
            <body>
             <form  method="??">
              <xsl:apply-templates select="subject"/>
              <xsl:apply-templates select="description"/>
              </form>
            <body>
</xsl:template>

  <xsl:template match="/subject">
    <h1></h1>
  <xsl:template >


</xsl:template>





