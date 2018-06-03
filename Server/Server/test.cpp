#include "winsgl.h"
#include <string>

SOCKET server, connection;

using std::string;

void mainHandler(string data) {
	string type = data.substr(0, data.find(':'));
	data = data.substr(data.find(':') + 1);
	if (type == "fab") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("fab.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << data << std::endl << std::endl;
		fout.close();
	}
	else if (type == "daily0") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily morning.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << std::endl;
		fout.close();
	}
	else if (type == "daily1") {

	}
	else if (type == "daily2") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily love.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << data << std::endl << std::endl;
		fout.close();
	}
	else if (type == "daily3") {

	}
	else if (type == "daily5") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily noon.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << std::endl;
		fout.close();
	}
	else if (type == "lover") {

	}
	else if (type == "joke") {

	}
	else if (type == "novel") {

	}
	else if (type == "secret") {

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

		SYSTEMTIME sys;
		GetLocalTime(&sys);

		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << buf << std::endl << std::endl;
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
	if (biosKey(1)) {
		hideToTray();
		clearKeyBuffer();
	}
	return;
}