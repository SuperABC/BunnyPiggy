#include "winsgl.h"
#include <string>
#include <atlimage.h>

SOCKET server, connection;

using std::string;

LPWSTR widen(const char *src) {
	int rt;
	LPWSTR rs;
	rt = MultiByteToWideChar(CP_ACP, 0, src, -1, NULL, 0);
	rs = (LPWSTR)malloc(rt * sizeof(wchar_t));
	MultiByteToWideChar(CP_ACP, 0, src, -1, rs, rt * sizeof(wchar_t));
	return rs;
}
string GBKToUTF8(const std::string& strGBK) {
	string strOutUTF8 = "";
	WCHAR * str1;
	int n = MultiByteToWideChar(CP_ACP, 0, strGBK.c_str(), -1, NULL, 0);
	str1 = new WCHAR[n];
	MultiByteToWideChar(CP_ACP, 0, strGBK.c_str(), -1, str1, n);
	n = WideCharToMultiByte(CP_UTF8, 0, str1, -1, NULL, 0, NULL, NULL);
	char * str2 = new char[n];
	WideCharToMultiByte(CP_UTF8, 0, str1, -1, str2, n, NULL, NULL);
	strOutUTF8 = str2;
	delete[] str1;
	str1 = NULL;
	delete[] str2;
	str2 = NULL;
	return strOutUTF8;
}
void writePic(const char *src, int width, int height) {
	CImage *pic = new CImage();
	pic->Create(width, height, 24);

	int i = 0;
	for (int k = 0; k < height; k++) {
		for (int j = 0; j < width; j++) {
			pic->SetPixelRGB(j, k, src[i++] * 2, src[i++] * 2, src[i++] * 2);
			i++;
		}
	}

	SYSTEMTIME sys;
	GetLocalTime(&sys);
	char fn[64];
	sprintf(fn, "Food\\%d-%d-%d-%d-%d-%d.jpg", sys.wYear, sys.wMonth, sys.wDay,
		sys.wHour, sys.wMinute, sys.wSecond);
	pic->Save(widen(fn));
}

void mainHandler(char *str, SOCKET socket) {
	string data = string(str);
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
	else if (type == "board") {
		std::ifstream fin("board.txt");

		string input, tmp;
		while (!fin.eof()) {
			getline(fin, tmp);
			input = input + tmp + '\n';
		}
		fin.close();

		socketSend(socket, GBKToUTF8(input).data());
	}
	else if (type == "boardok") {
		std::ifstream fin("board.txt", std::ios::out | std::ios::trunc);
		fin.close();
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
	else if (type == "daily2") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily love.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << data << std::endl << std::endl;
		fout.close();
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
	else if (type == "daily7") {

	}
	else if (type == "daily8") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily anger.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << data << std::endl << std::endl;
		fout.close();
	}
	else if (type == "daily9") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily dream.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << data << std::endl << std::endl;
		fout.close();
	}
	else if (type == "daily11") {
		SYSTEMTIME sys;
		GetLocalTime(&sys);

		std::ofstream fout("daily night.log", std::ios::app);
		fout << sys.wYear << '.' << sys.wMonth << '.' << sys.wDay << " " <<
			sys.wHour << ':' << sys.wMinute << ':' << sys.wSecond << '.' << sys.wMilliseconds << " weekday" <<
			sys.wDayOfWeek << std::endl << std::endl;
		fout.close();
	}
	else if (type == "loverr") {
		std::ifstream fin("lover.txt");
		string input, tmp;
		while (!fin.eof()) {
			getline(fin, tmp);
			if (data.find(tmp)==0) {
				string text;
				getline(fin, tmp);
				while (tmp.length()) {
					text += tmp + '\n';
					getline(fin, tmp);
				}
				socketSend(socket, GBKToUTF8(text).data());
				break;
			}
			else {
				while(tmp.length())getline(fin, tmp);
			}
		}
		fin.close();
	}
	else if (type == "loverw") {

	}
	else if (type == "joke") {

	}
	else if (type == "novel") {

	}
	else if (type == "secret") {

	}
	else if (type == "pic") {
		string widthStr = data.substr(0, data.find(' '));
		data = data.substr(data.find(' ') + 1);
		string heightStr = data.substr(0, data.find(' '));
		data = data.substr(data.find(' ') + 1);
		char *buf = new char[1024*1024];

		int i = 0;
		while (str[i++] != ' ');
		while (str[i++] != ' ');
		memcpy(buf, str + i, 64 - i);
		i = 64 - i;
		while (socketReceive(socket, buf + i, 1024) != SG_CONNECTION_FAILED) {
			Sleep(1);
			i += 1024;
		}
		writePic(buf, atoi(widthStr.data()), atoi(heightStr.data()));
		delete[] buf;
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
	char buf[64] = { 0 };

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

		mainHandler(buf, tmp);
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