<script	type="text/javascript">
	// @author Vlad	Skarzhevskyy
	// @version	$Revision$ ($Author$)

	var	jTidyReportHighlightLastLineNum	= null;
	var	jTidyReportHighlightBackgroundSave = "";

	function jTidyReportSetDefault(element)
	{
		if (element	!= null)
		{
			//element.style.color =	"";
			element.style.background = jTidyReportHighlightBackgroundSave;
			element.style.border = "";
		}
	}

	function jTidyReportSetHighlight(element)
	{
		if (element	!= null)
		{
			//element.style.color =	"red";
			jTidyReportHighlightBackgroundSave = element.style.background;
			element.style.background = "#F0C8C8";
			element.style.border = "1px solid red";
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