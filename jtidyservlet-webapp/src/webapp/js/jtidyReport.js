// Created on 17.08.2004 by vlads

// $Revision$
// $Date$
// $Author$
// $Workfile:   monitoring.js  $

var ppmForm;
var currentRequestID = 0;

function ppmGetById(doc, id) {
    if ((doc == null)
        || (id == null)
        || (id == '')) {
        return null;
    }
    return doc.getElementById(id);
}

function ppmElement(id) {
    return ppmGetById(document, id);
}

function ppmHide() {
    ppmForm.style.visibility = 'hidden';
    ppmForm.style.height = 0;
}

function ppmVisible() {
    if (ppmForm == null) {
        return false;
    }
    return (ppmForm.style.visibility == 'visible');
}

function ppmResize() {
    var ptb=ppmGetById(document, "outerTable");
    if (!ptb) return;
    ptb.style.visibility = 'visible';
    if (ppmForm == null) return;
    var w = ptb.offsetWidth;
    var h = ptb.offsetHeight;
    if (w) ppmForm.style.width = (4 + w) + "px";
    if (h) ppmForm.style.height = (4 + h) + "px";
}


function loadSource(id, http_url) {
	bufferFrame.document.location = http_url + "&notifyOnLoadID=" + id;
}

function loadSourceFinish(id) {
    var contents = ppmGetById(bufferFrame.document, 'contentsDiv');
    if (contents != null) {
	    ppmGetById(document, id).innerHTML = contents.innerHTML;
	    ppmResize();
    }
}

function childLoadSourceFinish(notifyOnLoadID) {
	if ((notifyOnLoadID != null) && (notifyOnLoadID != '') && (notifyOnLoadID != 'null')) {
		parent.loadSourceFinish(notifyOnLoadID);
	}
}

function ppmShow() {
	ppmForm = ppmGetById(parent.document, self.name);

	if (ppmVisible()) {
        ppmHide();
        return false;
    }

	ppmForm.style.left = 20;
	ppmForm.style.visibility = 'visible';
    ppmLoadTab('./iframe_body_report.jsp?requestID=' + currentRequestID);
    return false;
}

function ppmLoadTab(url) {
    ppmForm = ppmGetById(parent.document, self.name);
	ppmGetById(document, 'contentsDiv').innerHTML = 'Loading...<br>' + url;
	// Reset initial size so proper resize could be done.
	if (ppmForm != null) ppmForm.style.width = 200;
	loadSource('contentsDiv', url);
}