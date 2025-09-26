import java.awt.Color;
/*
 * Sam Deitz
 * 5/23/2023
 * class for roatating balls
 */

class Ball {
    double x,y;
    
    int size;
    Color color;

    Ball(double x, double y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }
}
