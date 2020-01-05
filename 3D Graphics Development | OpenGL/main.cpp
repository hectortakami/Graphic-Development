
// Autores:      Hector Manuel Takami Flores	
//               Hugo Vazquez Reyes

// Creado:     22 - Noviembre - 2019

// Proyecto:   Proyecto Final - Arte Cinetico

// Descripción:	

//				El siguiente codigo es parte de la evaluacion final de la materia 'Graficas Computacionales'          

//				con el objetivo de implementar todos los conocimientos precticos obtenidos en OpenGL



// ----------------------------------------------------------

// Librerías

// ---------------------------------------------------------


#include <Windows.h>


#include <iostream>


#ifdef __APPLE__
#include <OpenGL/OpenGL.h>
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif
#include <GL/GLU.h>
#include "imageloader.h"
#include <math.h>
#include <ctime> 
#include <algorithm> 
#include <vector> 
#include <fstream>

#include <string>
#include <stdarg.h>
#include <iostream>

using namespace std;



#ifdef __APPLE__

#include <GLUT/glut.h>

#else

#include <GL/glut.h>

#endif



// ----------------------------------------------------------

// Prototipos de función

// ----------------------------------------------------------

void display();

void specialKeys();



// ----------------------------------------------------------

// Variables globales

// ----------------------------------------------------------

double rotate_y = 35.0;

double rotate_x = -20;

double animate_x = -20;

double animate_y, height_y, profundity_z, profundity_x = 0;

double w, a = 0;

double zoom_z = 0.65;

bool is_lighted = false;

char s[30];




const int font = (int)GLUT_BITMAP_9_BY_15;



// ----------------------------------------------------------

// Valores que controlan las propiedades de los materiales e iluminacion

// ----------------------------------------------------------

int colorLuz = 5;

int colorSpecular = colorLuz;

int posicion_luz = 0;

bool ambiental = false;



float Noemit[4] = { 0.0, 0.0, 0.0, 1.0 };

float SphShininess = 75;		 // Exponenete especular



// Orientacion de origen para la luz ambiental

float posiciones[2][4] = { { -0.5, -1.0, 1.0, 1 } , { -0.5, -1.0, -0.3, 1 } }; // Trasera | Central
float luz[2][4] = { { 0.5, 1.0, -1.0, 1 } , { -0.9, -0.2, -0.8, 1 } };


float SphAmbDiff[5][4] = {       // Colores de luz ambiente y difusa



   {1.0, 1.0, 1.0, 1.0},         // Blanco

   {0.5, 0.5, 0.0, 1.0},         // Amarillo

   {0.0, 1.0, 0.0, 1.0},         // Verde

   {0.0, 0.5, 0.5, 1.0},         // Cyan

   {1.0, 0.0, 1.0, 1.0}         // Magenta



};



// Valores de iluminacion

float ambientLight[4] = { 0.2, 0.2, 0.2, 1.0 };

float Lt0amb[4] = { 0.3, 0.3, 0.3, 1.0 };

float Lt0diff[4] = { 1.0, 1.0, 1.0, 1.0 };

float Lt0spec[4] = { 1.0, 1.0, 1.0, 1.0 };

GLfloat directedLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };

float dirI[4] = { 1, 0, 0, 0 };            // Orientacion de origen para la luz dirigida (foco)
float dirII[4] = { 0, 0, 0, 1 };


// ----------------------------------------------------------

// Función de retrollamada que define la iluminacion 

// ----------------------------------------------------------

void iluminarEscena() {

	// Posicionamiento de la iluminacion

	glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, Lt0spec);

	glMaterialfv(GL_FRONT_AND_BACK, GL_EMISSION, Noemit);   // Turn off emission




	if (ambiental) {

		glLightfv(GL_LIGHT0, GL_POSITION, dirI);

	}

	else {

		glLightfv(GL_LIGHT0, GL_POSITION, posiciones[posicion_luz]); // Position is transformed by ModelView matrix
		glLightfv(GL_LIGHT0, GL_POSITION, luz[posicion_luz]);


	}





	glEnable(GL_DEPTH_TEST);   // Depth testing must be turned on



	glEnable(GL_LIGHTING);      // Enable lighting calculations

	glEnable(GL_LIGHT0);      // Turn on light #0.

	

	// Set global ambient light

	glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientLight);



	// Light 0 light values.  Its position is set in drawScene().

	glLightfv(GL_LIGHT0, GL_AMBIENT, Lt0amb);

	glLightfv(GL_LIGHT0, GL_DIFFUSE, Lt0diff);

	glLightfv(GL_LIGHT0, GL_DIFFUSE, directedLight);

	//glLightfv(GL_LIGHT0, GL_POSITION, luz[posicion_luz]);
	glLightfv(GL_LIGHT5, GL_DIFFUSE, dirII);

	glLightfv(GL_LIGHT0, GL_SPECULAR, Lt0spec);

}



// ----------------------------------------------------------

// Función de control de flujo en consola para cambiar la luz

// ----------------------------------------------------------

void menuIluminacion() {



	printf("\n MODIFICAR FUENTE DE LUZ");



	printf("\n\n1. Define el tipo de iluminacion \n\n\t[1] Global ( Ambiental ) \n\t[2] Fuente Directa");

	char tipo_luz[10];

	cout << "\n\nIngresa tu seleccion: ";

	cin >> tipo_luz;

	cout << "\tTu seleccion es: " << tipo_luz << endl;



	printf("\n\n2. Define el color de la iluminacion \n\n\t[1] Blanco\n\t[2] Amarillo\n\t[3] Verde\n\t[4] Cyan\n\t[5] Magenta");

	char color[10];

	cout << "\n\nIngresa tu seleccion: ";

	cin >> color;

	cout << "\tTu seleccion es: " << color << endl;



	printf("\n\n3. Define la posicion de la iluminacion \n\n\t[1] Trasera\n\t[2] Central");

	char posicion[10];

	cout << "\n\nIngresa tu seleccion: ";

	cin >> posicion;

	cout << "\tTu seleccion es: " << posicion << endl;







	if ((atoi(tipo_luz) == 1 || atoi(tipo_luz) == 2) &&

		(atoi(posicion) == 1 || atoi(posicion) == 2) &&

		(atoi(color) == 1 || atoi(color) == 2 || atoi(color) == 3 || atoi(color) == 4 || atoi(color) == 5)) {



		//Definir iluminacion seleccionada

		colorLuz = atoi(color) - 1;



		if (atoi(tipo_luz) == 1) {

			ambiental = true;

		}

		else {

			ambiental = false;

		}



		if (atoi(posicion) == 1) {

			posicion_luz = 0;

		}

		else {

			posicion_luz = 1;

		}





		glEnable(GL_LIGHTING);

		iluminarEscena();



		printf("\n\nLA ILUMINACION SE MODIFICO SATISFACTORIAMENTE :)\nEn caso de no ver cambios activa la luz en la escena presionando [ ESPACIO ]\n\n");





	}

	else {

		printf("\n\nOPCIONES INVALIDAS! INTENTA NUEVAMENTE\n");

		menuIluminacion();

	}

}



// ----------------------------------------------------------

// Función de retrollamada para la animacion del segundo visor

// ----------------------------------------------------------

void animacionPermanente(int value) {

	animate_y += 0.05;

	glutTimerFunc(100000, animacionPermanente, 0);

	glutPostRedisplay();

}



// ----------------------------------------------------------

// Función de retrollamada para renderizado de la escultura

// ----------------------------------------------------------

void dibujarEscultura() {

	// ----------------------------------------------------------

	// Seccion I

	// ----------------------------------------------------------
	


	glColorMaterial(GL_FRONT, GL_SHININESS);

	glEnable(GL_COLOR_MATERIAL);

	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, SphAmbDiff[colorLuz]);

	glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, SphAmbDiff[colorSpecular]);

	glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, SphShininess);



	glBegin(GL_QUADS);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.8125, -0.50, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.8125, 0.50, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.9375, 0.50, -0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.9375, -0.50, -0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.5625, 0.50, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.5625, 0.25, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.6875, 0.25, -0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.6875, 0.50, -0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.5625, -0.25, 0.0);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.5625, 0.0, 0.0);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.6875, 0.0, 0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.6875, -0.25, 0.0);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.5625, 0.50, 0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.5625, 0.0, 0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.6875, 0.0, 0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.6875, 0.50, 0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.8125, -0.50, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.8125, -0.25, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.8125, -0.25, 0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.8125, -0.50, 0.0);



	glColor3f(1.0, 0.563, 0.0);   glVertex3f(-0.6875, 0.50, -0.25);

	glColor3f(1.0, 0.390, 0.0);      glVertex3f(-0.6875, 0.25, -0.25);

	glColor3f(1.0, 0.601, 0.153);      glVertex3f(-0.6875, 0.25, 0.0);

	glColor3f(1.0, 0.627, 0.0);      glVertex3f(-0.6875, 0.50, 0.0);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-1.0, -0.25, -0.125);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-1.0, -0.00, -0.125);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-1.0, -0.00, 0.5);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-1.0, -0.25, 0.5);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.5625, 0.25, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.5625, 0.00, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.5625, 0.00, 0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.5625, 0.25, 0.0);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.5625, 0.50, 0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.5625, 0.00, 0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.5625, 0.00, 0.375);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.5625, 0.50, 0.375);



	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.5625, 0.0, 0.0);

	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.5625, -0.25, 0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.5625, -0.25, 0.125);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.5625, 0.0, 0.125);



	glEnd();



	// ----------------------------------------------------------

	// Seccion II

	// ----------------------------------------------------------



	glColorMaterial(GL_FRONT, GL_EMISSION);

	glEnable(GL_COLOR_MATERIAL);

	glBegin(GL_QUADS);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.500, 0.00, -0.125);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.500, 0.25, -0.125);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.375, 0.25, -0.125);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.375, 0.00, -0.125);



	glColor3f(1.0, 0.563, 0.0);    glVertex3f(-0.500, 0.00, -0.00);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.500, 0.25, -0.00);

	glColor3f(1.0, 0.627, 0.0);    glVertex3f(-0.375, 0.25, -0.00);

	glColor3f(1.0, 0.601, 0.153);     glVertex3f(-0.375, 0.00, -0.00);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.500, 0.00, -0.125);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.500, 0.25, -0.125);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.500, 0.25, -0.00);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.500, 0.00, -0.00);



	glColor3f(1.0, 0.563, 0.0);      glVertex3f(-0.375, 0.00, -0.125);

	glColor3f(1.0, 0.390, 0.0);      glVertex3f(-0.375, 0.25, -0.125);

	glColor3f(1.0, 0.627, 0.0);      glVertex3f(-0.375, 0.25, -0.00);

	glColor3f(1.0, 0.601, 0.153);     glVertex3f(-0.375, 0.00, -0.00);





	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.375, -0.25, -0.0);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.375, -0.50, -0.0);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(-0.250, -0.50, -0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(-0.250, -0.25, -0.0);



	glColor3f(1.0, 0.563, 0.0);      glVertex3f(-0.375, -0.25, 0.25);

	glColor3f(1.0, 0.390, 0.0);      glVertex3f(-0.375, -0.50, 0.25);

	glColor3f(1.0, 0.627, 0.0);      glVertex3f(-0.375, -0.50, -0.00);

	glColor3f(1.0, 0.601, 0.153);     glVertex3f(-0.375, -0.25, -0.00);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.00, -0.25, 0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.00, -0.50, 0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.25, -0.50, 0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.25, -0.25, 0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(-0.125, 0.00, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(-0.125, 0.50, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.25, 0.50, -0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.25, 0.00, -0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.125, 0.50, 0.0);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.125, -0.50, 0.0);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.25, -0.50, 0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.25, 0.50, 0.0);



	glColor3f(1.0, 0.563, 0.0);      glVertex3f(0.25, 0.00, -0.00);

	glColor3f(1.0, 0.390, 0.0);      glVertex3f(0.25, -0.25, -0.00);

	glColor3f(1.0, 0.627, 0.0);      glVertex3f(0.25, -0.25, 0.25);

	glColor3f(1.0, 0.601, 0.153);     glVertex3f(0.25, 0.00, 0.25);



	glColor3f(1.0, 0.563, 0.0);      glVertex3f(-0.25, 0.00, -0.00);

	glColor3f(1.0, 0.390, 0.0);      glVertex3f(-0.25, -0.25, -0.00);

	glColor3f(1.0, 0.627, 0.0);      glVertex3f(-0.25, -0.25, -0.25);

	glColor3f(1.0, 0.601, 0.153);     glVertex3f(-0.25, 0.00, -0.25);





	glEnd();



	// ----------------------------------------------------------

	// Seccion III

	// ----------------------------------------------------------



	glColorMaterial(GL_FRONT, GL_EMISSION);

	glEnable(GL_COLOR_MATERIAL);

	glBegin(GL_QUADS);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.9375, -0.50, -0.5);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.9375, -0.25, -0.5);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.9375, -0.25, 0.125);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.9375, -0.50, 0.125);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.9375, -0.25, 0.125);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.9375, -0.00, 0.125);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.9375, -0.00, 0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.9375, -0.25, 0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.9375, -0.25, -0.5);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.9375, -0.00, -0.5);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.9375, -0.00, -0.375);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.9375, -0.25, -0.375);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.5, -0.50, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.5, -0.25, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.5, -0.25, 0.0);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.5, -0.50, 0.0);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.5, -0.25, -0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.5, -0.00, -0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.5, -0.00, -0.125);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.5, -0.25, -0.125);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.5, 0.0, -0.5);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.5, 0.25, -0.5);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.5, 0.25, 0.125);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.5, 0.0, 0.125);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.5, -0.25, 0.0);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.5, -0.00, 0.0);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.5, -0.00, 0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.5, -0.25, 0.25);



	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.8125, -0.50, 0.25);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.8125, -0.00, 0.25);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.9375, -0.00, 0.25);

	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.9375, -0.50, 0.25);



	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.8125, -0.50, 0.125);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.8125, -0.00, 0.125);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.9375, -0.00, 0.125);

	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.9375, -0.50, 0.125);



	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.8125, -0.25, -0.5);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.8125, -0.00, -0.5);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.9375, -0.00, -0.5);

	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.9375, -0.25, -0.5);



	glColor3f(1.0, 0.563, 0.0);     glVertex3f(0.5, 0.00, -0.5);

	glColor3f(1.0, 0.390, 0.0);     glVertex3f(0.5, 0.25, -0.5);

	glColor3f(1.0, 0.627, 0.0);     glVertex3f(0.8125, 0.25, -0.5);

	glColor3f(1.0, 0.601, 0.153);    glVertex3f(0.8125, 0.00, -0.5);



	glEnd();



	// ----------------------------------------------------------

	// Bases

	// ----------------------------------------------------------



	glColorMaterial(GL_FRONT, GL_EMISSION);

	glEnable(GL_COLOR_MATERIAL);

	glBegin(GL_QUADS);



	// PISO

	glColor3f(0.566, 0.469, 0.344); //222,184,135



	glVertex3f(0.5, -0.70, -0.75);

	glVertex3f(0.5, -0.70, -0.25);

	glVertex3f(-1.0, -0.70, -0.25);

	glVertex3f(-1.0, -0.70, -0.75);



	glVertex3f(1.0, -0.70, -0.75);

	glVertex3f(1.0, -0.70, -0.50);

	glVertex3f(0.5, -0.70, -0.50);

	glVertex3f(0.5, -0.70, -0.75);



	glVertex3f(-1.0, -0.70, 0.75);

	glVertex3f(-1.0, -0.70, 0.50);

	glVertex3f(-0.5, -0.70, 0.50);

	glVertex3f(-0.5, -0.70, 0.75);



	glVertex3f(-0.5, -0.70, 0.75);

	glVertex3f(-0.5, -0.70, 0.25);

	glVertex3f(1.0, -0.70, 0.25);

	glVertex3f(1.0, -0.70, 0.75);



	glVertex3f(-1.0, -0.70, 0.75);

	glVertex3f(-1.0, -0.70, -0.75);

	glVertex3f(-1.25, -0.70, -0.75);

	glVertex3f(-1.25, -0.70, 0.75);



	glVertex3f(1.0, -0.70, 0.75);

	glVertex3f(1.0, -0.70, -0.75);

	glVertex3f(1.25, -0.70, -0.75);

	glVertex3f(1.25, -0.70, 0.75);



	//SECCION 1

	glColor3f(0.535, 0.267, 0.076); //	(210,105,30)

	glVertex3f(-1.0, -0.75, -0.25);

	glVertex3f(-1.0, -0.75, 0.5);

	glVertex3f(-0.5, -0.75, 0.5);

	glVertex3f(-0.5, -0.75, -0.25);



	//SECCION 2

	glVertex3f(1.0, -0.75, -0.5);

	glVertex3f(1.0, -0.75, 0.25);

	glVertex3f(0.5, -0.75, 0.25);

	glVertex3f(0.5, -0.75, -0.5);



	//SECCION 3

	glVertex3f(-0.5, -0.75, -0.25);

	glVertex3f(-0.5, -0.75, 0.25);

	glVertex3f(0.5, -0.75, 0.25);

	glVertex3f(0.5, -0.75, -0.25);



	// ----------------------------------------------------------

	// BORDERS

	// ----------------------------------------------------------

	glColor3f(0.354, 0.229, 0.109); //139,90,43



	glVertex3f(-1.0, -0.75, -0.25);

	glVertex3f(-1.0, -0.75, 0.50);

	glVertex3f(-1.0, -0.70, 0.50);

	glVertex3f(-1.0, -0.70, -0.25);



	glVertex3f(-0.5, -0.75, 0.25);

	glVertex3f(-0.5, -0.75, 0.50);

	glVertex3f(-0.5, -0.70, 0.50);

	glVertex3f(-0.5, -0.70, 0.25);



	glVertex3f(-0.5, -0.75, 0.50);

	glVertex3f(-1.0, -0.75, 0.50);

	glVertex3f(-1.0, -0.70, 0.50);

	glVertex3f(-0.5, -0.70, 0.50);



	glVertex3f(0.5, -0.75, -0.25);

	glVertex3f(-1.0, -0.75, -0.25);

	glVertex3f(-1.0, -0.70, -0.25);

	glVertex3f(0.5, -0.70, -0.25);



	glVertex3f(1.0, -0.75, 0.25);

	glVertex3f(-0.5, -0.75, 0.25);

	glVertex3f(-0.5, -0.70, 0.25);

	glVertex3f(1.0, -0.70, 0.25);



	glVertex3f(1.0, -0.75, -0.50);

	glVertex3f(1.0, -0.75, 0.25);

	glVertex3f(1.0, -0.70, 0.25);

	glVertex3f(1.0, -0.70, -0.50);



	glVertex3f(0.5, -0.75, -0.50);

	glVertex3f(0.5, -0.75, -0.25);

	glVertex3f(0.5, -0.70, -0.25);

	glVertex3f(0.5, -0.70, -0.50);



	glVertex3f(1.0, -0.75, -0.50);

	glVertex3f(0.5, -0.75, -0.50);

	glVertex3f(0.5, -0.70, -0.50);

	glVertex3f(1.0, -0.70, -0.50);



	glEnd();



	// Cubo Rectangular transparente

	glEnable(GL_BLEND);

	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	glTranslatef(-0.25, -0.125, 0.0);

	glColor4f(1.0, 1.0, 1.0, 0.4);

	glutSolidCube(0.25);

	glDisable(GL_BLEND);

}



// ----------------------------------------------------------

// Función de retrollamada para el renderizado de texto 

// ----------------------------------------------------------

void setOrthographicProjection() {

	glMatrixMode(GL_PROJECTION);

	glPushMatrix();

	glLoadIdentity();

	gluOrtho2D(0, 1200, 0, 750);

	glScalef(1, -1, 1);

	glTranslatef(0, -750, 0);

	glMatrixMode(GL_MODELVIEW);

}



void resetPerspectiveProjection() {

	glMatrixMode(GL_PROJECTION);

	glPopMatrix();

	glMatrixMode(GL_MODELVIEW);

}



void renderBitmapString(float x, float y, void* font, const char* string) {

	const char* c;

	glRasterPos2f(x, y);

	for (c = string; *c != '\0'; c++) {

		glutBitmapCharacter(font, *c);

	}

}



// ----------------------------------------------------------

// Función de retrollamada “display()”

// ----------------------------------------------------------

void display() {



	//  Borrar pantalla y Z-buffer

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);



	// Vision de la camara

	gluLookAt(0, 0, 0, a, w, 0, 0, 0, 0);





	// Resetear transformaciones

	glLoadIdentity();



	// Visor superior de la interaccion de usuario

	glViewport(0, 300, 1200, 500);



	// Rotar cuando el usuario cambie “rotate_x” y “rotate_y”

	glMatrixMode(GL_MODELVIEW);

	glPushMatrix();

	glLoadIdentity();

	glRotatef(rotate_x, 1.0, 0.0, 0.0);

	glRotatef(rotate_y, 0.0, 1.0, 0.0);

	glScalef(zoom_z, zoom_z, zoom_z);

	glTranslatef(0, height_y, profundity_z);

	glTranslatef(profundity_x, height_y, 0);

	dibujarEscultura();

	glPopMatrix();



	//Visor de animacion

	glViewport(0, -50, 600, 400);



	glMatrixMode(GL_MATRIX_MODE);

	glPushMatrix();

	glLoadIdentity();

	glRotatef(animate_x, 1.0, 0.0, 0.0);

	glRotatef(animate_y, 0.0, 1.0, 0.0);

	dibujarEscultura();

	glPopMatrix();



	// Animacion Permanente

	animacionPermanente(0);



	//Visor de texto con instrucciones

	glViewport(600, -50, 600, 400);

	glColor3d(1.0, 1.0, 1.0);

	setOrthographicProjection();

	glPushMatrix();

	glLoadIdentity();

	renderBitmapString(350, 150, (void*)font, "Proyecto Final - Arte Cinetico");

	renderBitmapString(100, 200, (void*)font, "Inspirado en la obra 'The Body of Color' de Helio Oiticica");

	renderBitmapString(550, 300, (void*)font, "CONTROLES");

	renderBitmapString(0, 355, (void*)font, " * Vista superior | Vista inferior           [       ^ | v      ]");

	renderBitmapString(0, 390, (void*)font, " * Rotar derecha  | Rotar izquierda          [       < | >      ]");

	renderBitmapString(0, 425, (void*)font, " * Trasladar camara ( Vertical | Horizontal) [   W | A | S | D  ]");

	renderBitmapString(0, 460, (void*)font, " * Zoom-in                                   [         +        ]");

	renderBitmapString(0, 495, (void*)font, " * Zoom-out                                  [         -        ]");

	renderBitmapString(0, 530, (void*)font, " * (ON|OFF) Iluminacion predefinida          [      ESPACIO     ]");

	renderBitmapString(0, 565, (void*)font, " * Modificar fuente de luz (Ver terminal)    [       y | Y      ]");

	renderBitmapString(0, 600, (void*)font, " * Salir                                     [   ESC | q | Q    ]");

	glPopMatrix();

	resetPerspectiveProjection();





	if (!is_lighted)

	{

		glDisable(GL_LIGHTING);



	}

	else

	{

		glEnable(GL_LIGHTING);

		iluminarEscena();

	}



	glFlush();

	glutSwapBuffers();







}



// ----------------------------------------------------------

// Función de retrollamada “specialKeys()” 

// ----------------------------------------------------------

void specialKeys(int key, int x, int y) {



	//  Flecha derecha: aumentar rotación 5 grados

	if (key == GLUT_KEY_RIGHT)

		rotate_y += 5;



	//  Flecha izquierda: disminuir rotación 5 grados

	else if (key == GLUT_KEY_LEFT)

		rotate_y -= 5;



	else if (key == GLUT_KEY_UP)

		rotate_x += 5;



	else if (key == GLUT_KEY_DOWN)

		rotate_x -= 5;





	//  Solicitar actualización de visualización

	glutPostRedisplay();

}



// ----------------------------------------------------------

// Función de retrollamada “keyboard()” 

// ----------------------------------------------------------

void keyboard(unsigned char key, int x, int y)

{

	switch (key)

	{

	case 27:

	case 'q':

	case 'Q':

		exit(0);

		break;



	case ' ':

		is_lighted = !is_lighted;

		break;



	case '+':

		zoom_z += 0.05;

		break;



	case '-':

		zoom_z -= 0.05;

		break;



	case 'w':

	case 'W':

		height_y += 0.05;

		break;



	case 's':

	case 'S':

		height_y -= 0.05;

		break;



	case 'a':

	case 'A':



		if ((int)rotate_y % 180 == 0) {

			profundity_x += 0.05;

			break;

		}



		if ((int)rotate_y % 90 == 0) {

			profundity_z += 0.05;

			break;

		}



		if ((int)rotate_y % 270 == 0) {

			profundity_z += 0.05;

			break;

		}



	case 'd':

	case 'D':



		if ((int)rotate_y % 180 == 0) {

			profundity_x -= 0.05;

			break;

		}



		if ((int)rotate_y % 90 == 0) {

			profundity_z -= 0.05;

			break;

		}



		if ((int)rotate_y % 270 == 0) {

			profundity_z -= 0.05;

			break;

		}



	case 'y':

	case 'Y':

		menuIluminacion();

		break;





	default:

		break;

	}



	glutPostRedisplay();

}



// ----------------------------------------------------------

// Función “main()”

// ----------------------------------------------------------

int main(int argc, char* argv[]) {



	//  Inicializar los parámetros GLUT y de usuario proceso

	glutInit(&argc, argv);

	glMatrixMode(GL_PROJECTION);

	glLoadIdentity();

	glOrtho(0.0f, 1200, 600, 0.0f, 0.0f, 1.0f);



	//  Solicitar ventana con color real y doble buffer con Z-buffer 

	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);



	// Crear ventana

	glutInitWindowPosition(10, 10);

	glutInitWindowSize(1200, 750);

	glutCreateWindow("Helio Oiticica");







	//  Habilitar la prueba de profundidad de Z-buffer

	glEnable(GL_DEPTH_TEST);



	// Funciones de retrollamada

	glutDisplayFunc(display);

	glutSpecialFunc(specialKeys);

	glutKeyboardFunc(keyboard);



	//  Pasar el control de eventos a GLUT

	glutMainLoop();



	//  Regresar al sistema operativo

	return 0;



}
