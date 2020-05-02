/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileNotFoundException;

/**
 *
 * @author tchoa
 */
public class Player extends Entity{
    Player(int x, int y, int width, int height, String name, String directory) throws FileNotFoundException{
        super(x, y, width, height, name, directory);
        this.setVisual(directory+"\\src\\javafxgame\\GameArt\\Player.png");
        System.out.println("Player physics made");
    }
    @Override
    public void updateLocation(double time){
    }
}
