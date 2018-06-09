/*
	This is an enhanced version of a1-basic -- a Frogger game.
	1. Change the color of the frogger and the blocks
		  (I choose use the same color for first/last moving blocks because this look better)
	2. Give an instruction showing on screen when the game is finished (all level)

	Author : Yuxiao Yu
	Date: Jan 31, 2018
*/

#include <cstdlib>
#include <iostream>
#include <unistd.h>
#include <sys/time.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <sys/time.h>
#include <vector>
#include <string>

// get microseconds
unsigned long now() {
	timeval tv;
	gettimeofday(&tv, NULL);
	return tv.tv_sec * 1000000 + tv.tv_usec;
}

using namespace std;

Colormap screen_colormap;
XColor red, pink, blue, purple;
Status rc;

const int BufferSize = 10;
unsigned int maxHeight = 250;
unsigned int maxWidth = 850;
int FPS = 30;

struct XInfo {
  Display*  display;
  Window   window;
  GC       gc[3];
};

class Displayable {
public:
  virtual void paint(XInfo& xinfo) = 0;
  virtual int getX() = 0;
  virtual int getY() = 0;
  virtual void setX(int next) = 0;
  virtual void setY(int next) = 0;
  virtual int getLevel() {};
  virtual void setLevel(int next) = 0;
};

class Block : public Displayable {
public:
	virtual void paint(XInfo& xinfo) {

		// if this is the frog
		if (identifier == 0) {
			XSetFillStyle(xinfo.display, xinfo.gc[1], FillSolid);
			XFillRectangle(xinfo.display, xinfo.window, xinfo.gc[1], x, y, w, h);

		// if this is the median row of blocks	
		} else if (identifier == 2){
			cout << "we are in red now" << endl;
			XSetFillStyle(xinfo.display, xinfo.gc[2], FillSolid);
			XFillRectangle(xinfo.display, xinfo.window, xinfo.gc[2], x, y, w, h);

		// other two row of blocks	
		} else {
			XSetFillStyle(xinfo.display, xinfo.gc[0], FillSolid);
			XFillRectangle(xinfo.display, xinfo.window, xinfo.gc[0], x, y, w, h);
		}
	}

	// Constructor()
	Block(int x, int y, int w, int h, int identifier): x(x), y(y), w(w), h(h), identifier(identifier) {}

	virtual int getX() {
		return x;
	}

	virtual int getY() {
		return y;
	}

	virtual void setX(int next) {
		x = next;
	}

	virtual void setY(int next) {
		y = next;
	}

	virtual int getLevel() {};
	virtual void setLevel(int next) {} ;


private:
	int x;
	int y;
	int w;
	int h;
	int identifier;
};

class Text : public Displayable {
public:	
	virtual void paint(XInfo& xinfo) {
		string out;
		int length = 7;
		if (level == 1) {
			out = "Level 1";
		} else if (level == 2) {
			out = "Level 2";
		} else if (level == 3) {
			out = "Level 3";
		} else if (level == 4){
			out = "Level 4";
		} else {
			out = "You are done! Press q to quit.";
			length = 30;
		}

        XDrawImageString( xinfo.display, xinfo.window, xinfo.gc[1],
                          this->x, this->y, out.c_str(), length); // string length is always 7
    }

    Text(int x, int y, int level): x(x), y(y), level(level)  {}

    virtual int getX() {};
    virtual int getY() {};
    virtual void setX(int next) {};
    virtual void setY(int next) {};

    virtual int getLevel() {
    	return level;
    }
    virtual void setLevel(int next) {
    	level = next;
    }

private:
    int x;
    int y;
    int level;
};

void error( string str ) {
  cerr << str << endl;
  exit(0);
}

void repaint(vector<Displayable*> dList, Displayable *frog, XInfo& xinfo) {
  vector<Displayable*>::const_iterator begin = dList.begin();
  vector<Displayable*>::const_iterator end = dList.end();
  XClearWindow(xinfo.display, xinfo.window);
  while ( begin != end ) {
    Displayable* d = *begin;
    d->paint(xinfo);
    begin++;
  }
  if (frog != NULL) {
  	frog->paint(xinfo);
  }
  XFlush(xinfo.display);
}

int checkCollision(vector<Displayable*> dList, Displayable *frog) {
	int cur_place = frog->getX();
	int cur_right = frog->getX() + 50;
	int collision = 0;
	if (frog->getY() == 150) {

		if ((dList[1]->getX() <= cur_place && cur_place < dList[1]->getX()+100) ||
			(dList[2]->getX() <= cur_place && cur_place < dList[2]->getX()+100) ||
			(dList[10]->getX() <= cur_place && cur_place < dList[10]->getX()+100) ||
			(dList[1]->getX() <= cur_right && cur_right < dList[1]->getX()+100) ||
			(dList[2]->getX() <= cur_right && cur_right < dList[2]->getX()+100) ||
			(dList[10]->getX() <= cur_right && cur_right < dList[10]->getX()+100)) {
			collision = 1;
		}

	} 

	if (frog->getY() == 100) {

		if ((dList[3]->getX() <= cur_place && cur_place < dList[3]->getX()+20) ||
			(dList[4]->getX() <= cur_place && cur_place < dList[4]->getX()+20) ||
			(dList[5]->getX() <= cur_place && cur_place < dList[5]->getX()+20) ||
			(dList[6]->getX() <= cur_place && cur_place < dList[6]->getX()+20) ||
			(dList[11]->getX() <= cur_place && cur_place < dList[11]->getX()+20) ||
			(dList[3]->getX() <= cur_right && cur_right < dList[3]->getX()+20) ||
			(dList[4]->getX() <= cur_right && cur_right < dList[4]->getX()+20) ||
			(dList[5]->getX() <= cur_right && cur_right < dList[5]->getX()+20) ||
			(dList[6]->getX() <= cur_right && cur_right < dList[6]->getX()+20) ||
			(dList[11]->getX() <= cur_right && cur_right < dList[11]->getX()+20) ||
			(dList[3]->getX() >= cur_place && dList[3]->getX()+20 <= cur_right) ||
			(dList[4]->getX() >= cur_place && dList[4]->getX()+20 <= cur_right) ||
			(dList[5]->getX() >= cur_place && dList[5]->getX()+20 <= cur_right) ||
			(dList[6]->getX() >= cur_place && dList[6]->getX()+20 <= cur_right) ||
			(dList[11]->getX() >= cur_place && dList[11]->getX()+20 < cur_right)) {
			collision = 1;
		}
	} 

	if (frog->getY() == 50) {

		if ((dList[7]->getX() <= cur_place && cur_place < dList[7]->getX()+50) ||
			(dList[8]->getX() <= cur_place && cur_place < dList[8]->getX()+50) ||
			(dList[9]->getX() <= cur_place && cur_place < dList[9]->getX()+50) ||
			(dList[12]->getX() <= cur_place && cur_place < dList[12]->getX()+50) ||
			(dList[7]->getX() <= cur_right && cur_right < dList[7]->getX()+50) ||
			(dList[8]->getX() <= cur_right && cur_right < dList[8]->getX()+50) ||
			(dList[9]->getX() <= cur_right && cur_right < dList[9]->getX()+50) ||
			(dList[12]->getX() <= cur_right && cur_right < dList[12]->getX()+50)) {

			collision = 1;
		}
	}

	return collision;

}

void moveBlock(vector<Displayable*> dList, int speed) {
	if (dList[10]->getX() != -101) {

		if (dList[10]->getX() + speed < maxWidth) {
			dList[1]->setX(dList[1]->getX() + speed);
			dList[2]->setX(dList[2]->getX() + speed);
			dList[10]->setX(dList[10]->getX() + speed);
		} else {
			dList[10]->setX(-101);
			dList[1]->setX(dList[1]->getX() + speed);
			dList[2]->setX(dList[2]->getX() + speed);
		}

	} else if (dList[1]->getX() + speed + 100 > maxWidth) {

		dList[10]->setX(dList[1]->getX() + speed);

		dList[1]->setX(dList[1]->getX() + speed - maxWidth);
		dList[2]->setX(dList[2]->getX() + speed);
	} else if (dList[2]->getX() + speed + 100 > maxWidth) {
		//cout << "This block is out of range with x " << dList[2]->getX() << endl;
		dList[10]->setX(dList[2]->getX() + speed);

		dList[2]->setX(dList[2]->getX() + speed - maxWidth);
		dList[1]->setX(dList[1]->getX() + speed);
	} else {

		dList[1]->setX(dList[1]->getX() + speed);
		dList[2]->setX(dList[2]->getX() + speed);
		//cout << "x is " << dList[2]->getX() << endl;
	}

	if (dList[11]->getX() != -101) {

		if (dList[11]->getX() - speed >= -20) {
			dList[3]->setX(dList[3]->getX() - speed);
			dList[4]->setX(dList[4]->getX() - speed);
			dList[5]->setX(dList[5]->getX() - speed);
			dList[6]->setX(dList[6]->getX() - speed);
			dList[11]->setX(dList[11]->getX() - speed);
		} else {
			dList[11]->setX(-101);
			dList[3]->setX(dList[3]->getX() - speed);
			dList[4]->setX(dList[4]->getX() - speed);
			dList[5]->setX(dList[5]->getX() - speed);
			dList[6]->setX(dList[6]->getX() - speed);
		}

	} else if (dList[3]->getX() - speed < 0) {
		dList[11]->setX(dList[3]->getX() - speed);

		dList[3]->setX(maxWidth + dList[3]->getX() - speed);
		dList[4]->setX(dList[4]->getX() - speed);
		dList[5]->setX(dList[5]->getX() - speed);
		dList[6]->setX(dList[6]->getX() - speed);

	} else  if (dList[4]->getX() - speed < 0) {
		dList[11]->setX(dList[4]->getX() - speed);

		dList[4]->setX(maxWidth + dList[4]->getX() - speed);
		dList[3]->setX(dList[3]->getX() - speed);
		dList[5]->setX(dList[5]->getX() - speed);
		dList[6]->setX(dList[6]->getX() - speed);

	} else if (dList[5]->getX() - speed < 0) {
		dList[11]->setX(dList[5]->getX() - speed);

		dList[5]->setX(maxWidth + dList[5]->getX() - speed);
		dList[3]->setX(dList[3]->getX() - speed);
		dList[4]->setX(dList[4]->getX() - speed);
		dList[6]->setX(dList[6]->getX() - speed);

	} else if (dList[6]->getX() - speed < 0) {
		dList[11]->setX(dList[6]->getX() - speed);

		dList[6]->setX(maxWidth + dList[6]->getX() - speed);
		dList[3]->setX(dList[3]->getX() - speed);
		dList[4]->setX(dList[4]->getX() - speed);
		dList[5]->setX(dList[5]->getX() - speed);
	} else {
		dList[3]->setX(dList[3]->getX() - speed);
		dList[4]->setX(dList[4]->getX() - speed);
		dList[5]->setX(dList[5]->getX() - speed);
		dList[6]->setX(dList[6]->getX() - speed);
	}

	if (dList[12]->getX() != -101) {
		if (dList[12]->getX() + speed < maxWidth) {
			dList[7]->setX(dList[7]->getX() + speed);
			dList[8]->setX(dList[8]->getX() + speed);
			dList[9]->setX(dList[9]->getX() + speed);
			dList[12]->setX(dList[12]->getX() + speed);
		} else {
			dList[12]->setX(-101);
			dList[7]->setX(dList[7]->getX() + speed);
			dList[8]->setX(dList[8]->getX() + speed);
			dList[9]->setX(dList[9]->getX() + speed);
		}

	} else if (dList[7]->getX() + speed + 50 > maxWidth) {
		dList[12]->setX(dList[7]->getX() + speed);

		dList[7]->setX(dList[7]->getX() + speed - maxWidth);
		dList[8]->setX(dList[8]->getX() + speed);
		dList[9]->setX(dList[9]->getX() + speed);

	} else if (dList[8]->getX() + speed + 50 > maxWidth) {
		dList[12]->setX(dList[8]->getX() + speed);

		dList[8]->setX(dList[8]->getX() + speed  - maxWidth);
		dList[7]->setX(dList[7]->getX() + speed);
		dList[9]->setX(dList[9]->getX() + speed);

	} else if (dList[9]->getX() + speed + 50 > maxWidth){
		dList[12]->setX(dList[9]->getX() + speed);

		dList[9]->setX(dList[9]->getX() + speed  - maxWidth);
		dList[7]->setX(dList[7]->getX() + speed);
		dList[8]->setX(dList[8]->getX() + speed);
	} else {

		dList[7]->setX(dList[7]->getX() + speed);
		dList[8]->setX(dList[8]->getX() + speed);
		dList[9]->setX(dList[9]->getX() + speed);
	}
}

void eventloop(XInfo& xinfo, vector<Displayable*> dList, Displayable *frog) {
	cout << "FPS when entering the eventloop is: " << FPS << endl;
    XEvent event;
    KeySym key;
    char text[BufferSize];

	int FrogSize = 50;
	int initX = 0.5 * (maxWidth - FrogSize);
	int initY = maxHeight - FrogSize;

	unsigned long lastRepaint = 0;

    while ( true ) {
        //XNextEvent( xinfo.display, &event);
        if (XPending(xinfo.display) > 0) { 

			XNextEvent( xinfo.display, &event);

        	switch ( event.type ) {
        		// do nothing where mouse clicked
        		case ButtonPress:
            		break;

        		// do nothing where mouse moved
        		case MotionNotify:
            		break;
        		/*
         		* Exit when 'q' is typed.
         		* Arguments for XLookupString :
         		*                 event - the keyboard event
         		*                 text - buffer into which text will be written
         		*                 BufferSize - size of text buffer
         		*                 key - workstation independent key symbol
         		*                 0 - pointer to a composeStatus structure
         		*/
        		case KeyPress:
            		int i = XLookupString((XKeyEvent*)&event, text, BufferSize, &key, 0 );

            		if ( i == 1 && text[0] == 'q' ) {
            	    	cout << "Terminated normally." << endl;
            	    	XCloseDisplay(xinfo.display);
            	    	exit(0);
            		}

            		if (frog->getY() == 0 && i == 1 && text[0] == 'n') {
            			int current = dList[0]->getLevel();

            			if (current == 4) {
            				cout << "Level cannot be increased any more" << endl;
            				break;
            			}

            			frog->setX(initX);
            			frog->setY(initY);
            			
            			dList[0]->setLevel(current+1);
            			repaint(dList, frog, xinfo);
            		}

            		switch(key){
                		case XK_Up:
                    		cout << "Up" << endl;
                    
                    		if (frog->getY() != 0) {
                    			frog->setY(frog->getY() - 50);

                    		}

                    		break;
                		case XK_Down:
                    		cout << "Down" << endl;

                    		if (frog->getY() != initY && frog->getY() != 0) {
                    			frog->setY(frog->getY() + 50);
                   		 	}

                    		break;
                		case XK_Left:
                    		cout << "Left" << endl;

                    		if (frog->getX() != 0) {
                    			frog->setX(frog->getX() - 50);
               			    }

                    		break;
                		case XK_Right:
                    		cout << "Right" << endl;

                    		if (frog->getX() != maxWidth - FrogSize) {
                    			frog->setX(frog->getX() + 50);
                   		 	}

                    		break;
            		}
            		break;
        	}

        }
        unsigned long end = now();

        if (end - lastRepaint > 1000000 / FPS) {
        	// clear background
			XClearWindow(xinfo.display, xinfo.window);

			int speed = dList[0]->getLevel();
			moveBlock(dList, speed);

			int cur_level = dList[0]->getLevel();
			int collision = checkCollision(dList, frog);
			if (collision) {
				frog->setX(initX);
				frog->setY(initY);

				if (cur_level != 1 ) {
					dList[0]->setLevel(cur_level-1);
				}
			}

			repaint(dList, frog, xinfo);
			XFlush( xinfo.display );
			lastRepaint = now();
       	}

       	if (XPending(xinfo.display) == 0) {
			usleep(1000000 / FPS - (end - lastRepaint));
		}

		if (dList[0]->getLevel() == 4 && frog->getY() == 0) {
			Displayable * finish = new Text(320, 100, 5);
			dList.push_back(finish);
		}

    }
}

void initWin(int argc, char* argv[], XInfo& xinfo) {
	xinfo.display = XOpenDisplay("");

	if (xinfo.display == NULL) {
		error( "Can't open display." );
	}

	int screen = DefaultScreen(xinfo.display);
	long background = WhitePixel(xinfo.display, screen);
	long foreground = BlackPixel(xinfo.display, screen);
	xinfo.window = XCreateSimpleWindow(xinfo.display, DefaultRootWindow(xinfo.display),
	                             10, 10, 850, 250, 2, foreground, background);

	XSetStandardProperties(
		xinfo.display,
		xinfo.window,
		"Frog",
		"Frog",
		None,
		argv, argc, NULL);

	XSelectInput(xinfo.display, xinfo.window,
	             ButtonPressMask | KeyPressMask | EnterWindowMask | LeaveWindowMask | StructureNotifyMask); // select event;
	XMapRaised(xinfo.display, xinfo.window);
	XFlush(xinfo.display);
	sleep(1);  // This is important
}

int main( int argc, char *argv[] ) {
	XInfo xinfo;
	// Create the window and name it
	initWin(argc, argv, xinfo);

	// Create the forg
	int FrogSize = 50;
	int initX = 0.5 * (maxWidth - FrogSize);
	int initY = maxHeight - FrogSize;
	int screen = DefaultScreen(xinfo.display);

	screen_colormap = DefaultColormap(xinfo.display, DefaultScreen(xinfo.display));

	rc = XAllocNamedColor(xinfo.display, screen_colormap, "pink", &pink, &pink);
	GC gc = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	XSetForeground(xinfo.display, gc, pink.pixel);
	XSetBackground(xinfo.display, gc, WhitePixel(xinfo.display, screen));

	rc = XAllocNamedColor(xinfo.display, screen_colormap, "purple", &purple, &purple);
	GC gc1 = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	XSetForeground(xinfo.display, gc1, purple.pixel);
	XSetBackground(xinfo.display, gc1, WhitePixel(xinfo.display, screen));

	rc = XAllocNamedColor(xinfo.display, screen_colormap, "red", &red, &red);
	GC gc2 = XCreateGC(xinfo.display, xinfo.window, 0, 0);
	XSetForeground(xinfo.display, gc2, red.pixel);
	XSetBackground(xinfo.display, gc2, WhitePixel(xinfo.display, screen));

	xinfo.gc[0] = gc;
	xinfo.gc[1] = gc1;
	xinfo.gc[2] = gc2;

	// Create all other shapes 
	vector<Displayable *> dList;

	dList.push_back(new Text(780, 20, 1));

	// draw first row of moving  blocks 
	dList.push_back(new Block(0, 150, 100, 50, 1));
	dList.push_back(new Block(425, 150, 100, 50, 1));

	// draw second row of moving  blocks
	dList.push_back(new Block(0, 100, 20, 50, 2));
	dList.push_back(new Block(210, 100, 20, 50, 2));
	dList.push_back(new Block(425, 100, 20, 50, 2));
	dList.push_back(new Block(630, 100, 20, 50, 2));
	
	// draw third row of moving  blocks
	dList.push_back(new Block(0, 50, 50, 50, 3));
	dList.push_back(new Block(280, 50, 50, 50, 3));
	dList.push_back(new Block(560, 50, 50, 50, 3));

	dList.push_back(new Block(-101, 150, 100, 50, 1));
	dList.push_back(new Block(-101, 100, 20, 50, 2));
	dList.push_back(new Block(-101, 50, 50, 50, 3));

	Displayable *frog = new Block(initX, initY, FrogSize, FrogSize, 0);
	repaint(dList, frog, xinfo);
	XFlush(xinfo.display);

	if (argc > 1) {
		if (argc > 2) {
			error("Invalid input");
		}

		FPS = atoi(argv[1]); 
	}

	eventloop(xinfo, dList, frog);
	XCloseDisplay(xinfo.display);
}
