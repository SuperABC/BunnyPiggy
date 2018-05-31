#include "winsgl.h"

SOCKET server, connection;

using std::string;

void mainHandler(string data) {
	string type = data.substr(0, data.find(':'));
	if (type == "fab") {

	}
	if (type == "daily") {

	}
	if (type == "lover") {

	}
	if (type == "joke") {

	}
	if (type == "novel") {

	}
	if (type == "secret") {

	}
}

void layoutWidget() {
	widgetObj *Output;

	Output = newWidget(SG_OUTPUT, (SGstring)"Output");
	Output->pos.x = 40;
	Output->pos.y = 40;
	Output->size.x = 560;
	Output->size.y = 400;
	strcpy((char *)Output->content, "");
	registerWidget(Output);
}
void singleCommun(void) {
	SOCKET tmp = connection;
	char buf[64];

	if (socketReceive(tmp, buf, 64) != SG_CONNECTION_FAILED) {
		widgetObj *output = getWidgetByName("Output");
		strcpy((char *)output->content, buf);
		std::ofstream fout("socket.log", std::ios::app);
		fout << buf << std::endl;
		fout.close();
		mainHandler(buf);
	}
	closeSocket(tmp);
}
void resposeReq(void) {
	while (1) {
		connection = acceptOne(server);
		createThread(singleCommun);
	}
}

void sgSetup() {
	initWindow(640, 480, "SGL Sample", BIT_MAP);
	initMouse(SG_COORDINATE);
	initKey();
	layoutWidget();

	server = createServer(4497);
	createThread(resposeReq);
}
void sgLoop() {
	return;
}