<script	type="text/javascript">
	// @author Vlad	Skarzhevskyy
	// @version	$Revision$ ($Author$)

    var jTidyReportHighlightLastLineNum = null;

	function jTidyReportSetDefault(element)
	{
		if (element	!= null)
		{
			element.className = "JTidyReportSrcLineError";
		}
	}

	function jTidyReportSetHighlight(element)
	{
		if (element	!= null)
		{
			element.className = "JTidyReportSrcLineErrorSelected";
		}
	}

	function jTidyReportHighlight(lineNum) {
		if (jTidyReportHighlightLastLineNum	!= null)
		{
			jTidyReportSetDefault(document.getElementById(jTidyReportHighlightLastLineNum));
		}

		var	line = "srcline" + lineNum;
		jTidyReportSetHighlight(document.getElementById(line));
		jTidyReportHighlightLastLineNum	= line;
	}
</script>