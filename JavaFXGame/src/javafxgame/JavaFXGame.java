/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
public class JavaFXGame extends Application {
    int gamewidth;
    int gameheight;
    int screenwidth;
    int screenheight;
    String directory = System.getProperty("user.dir");
    IntegerProperty currentPane = new SimpleIntegerProperty(); // Main Menu = 0, currentLevel = 1, Pause = 2, Endscreen = 3
    ArrayList<Menu> menus = new ArrayList<Menu>();
    ArrayList<Level> levels = new ArrayList<Level>();
    boolean started;
    boolean failed;
    int score;
    int difficulty = 3;
    Scene scene;
    Stage stage;
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        System.out.println(directory);
        this.stage = primaryStage;
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        this.screenwidth = (int) primaryScreenBounds.getWidth();
        this.screenheight = (int) primaryScreenBounds.getHeight();
        createMainMenu();
        createPauseMenu();
        createEndScreen();
        levelOne();
        levelTwo();
        levelThree();
        this.currentPane.set(0);
        this.scene = new Scene(getScreen(null),screenwidth,screenheight);
        currentPane.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println(currentPane);
                if(newValue.toString().equals("1")){
                    try {
                        gameLoop(levels.get(0));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    currentPane.removeListener(this);
                }
                if(newValue.toString().equals("2")){
                    try {
                        gameLoop(levels.get(1));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    currentPane.removeListener(this);
                }
                if(newValue.toString().equals("3")){
                    try {
                        gameLoop(levels.get(2));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(JavaFXGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                    currentPane.removeListener(this);
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    //Generates the Main Menu shown at the start of the game
    private void createMainMenu() throws FileNotFoundException{
        Menu main = new Menu("Main",0,0,this.screenwidth,this.screenheight);
        
        HBox buttonBox = new HBox();
        buttonBox.setPrefWidth(screenwidth);
        buttonBox.setPrefHeight(screenheight/10);
        buttonBox.setLayoutY(screenheight*0.7);
        buttonBox.setLayoutX(0);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(screenwidth*0.05);
        
        ImageView button1 = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\Level1.png")));
        button1.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(1);
            }
        });
        buttonBox.getChildren().add(button1);
        
        ImageView button2 = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\Level2.png")));
        button2.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(2);
            }
        });
        buttonBox.getChildren().add(button2);
        
        ImageView button3 = new ImageView(new Image(new FileInputStream(this.directory+"\\src\\javafxgame\\GameArt\\Level3.png")));
        button3.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                currentPane.set(3);
            }
        });
        buttonBox.getChildren().add(button3);
        
        main.getChildren().add(buttonBox);
        menus.add(main);
    }
    //Generates the Pause screen shown when a player pauses the game
    private void createPauseMenu(){
        Menu pause = new Menu("Pause",0,0,this.screenwidth,this.screenheight);
        menus.add(pause);
    }
    //Generates the End screen shown when a level is completed
    private void createEndScreen(){
        Menu end = new Menu("End",0,0,this.screenwidth,this.screenheight);
        menus.add(end);
    }
    //Creates the first level, then adds it to the global list
    private void levelOne() throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelOne = new Level(gamewidth, gameheight, this.directory);
        levelOne.setBackground("\\src\\javafxgame\\GameArt\\Background.png");
        levelOne.createEntity(0, 0, 1920, 1080 , "Camera");
        levelOne.createEntity(30, (int) (levelOne.camera.getY()+(levelOne.camera.height/2)), 25, 25, "Player"); 
        levelOne.camera.createGUI(screenwidth,screenheight,score);
        generateEnemies(levelOne);
        levelOne.buildVisuals();
        levels.add(levelOne);
    }
    //Creates the second level, then adds it to the global list
    private void levelTwo() throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelTwo = new Level(gamewidth, gameheight, this.directory);
        levelTwo.setBackground("\\src\\javafxgame\\GameArt\\Game.png");
        levelTwo.createEntity(0, 0, 1920, 1080 , "Camera");
        levelTwo.createEntity(30, (int) (levelTwo.camera.getY()+(levelTwo.camera.height/2)), 25, 25, "Player"); 
        levelTwo.camera.createGUI(screenwidth,screenheight, score);
        levelTwo.buildVisuals();
        levels.add(levelTwo);
    }
    //Creates the third level, then adds it to the global list
    private void levelThree() throws FileNotFoundException{
        this.gamewidth = 20000;
        this.gameheight = 1200;
        Level levelThree = new Level(gamewidth, gameheight, this.directory);
        levelThree.setBackground("\\src\\javafxgame\\GameArt\\Game.png");
        levelThree.createEntity(0, 0, 1920, 1080 , "Camera");
        levelThree.createEntity(30, (int) (levelThree.camera.getY()+(levelThree.camera.height/2)), 25, 25, "Player"); 
        levelThree.camera.createGUI(screenwidth,screenheight, score);
        levelThree.buildVisuals();
        levels.add(levelThree);
    }
    //When called, returns the currentPane that is to be displayed. Lets me easily switch between the pause screen and the normal game, and allows the 
    //option to switch in-between levels if desired
    private Pane getScreen(Level currentLevel){
        switch(currentPane.get()){
            case 0:
                return menus.get(0);
            case 1:
                return currentLevel.getPane();
            case 2:
                return menus.get(1);
            case 3:
                return menus.get(2);
        }
        return null;
    }
    private void generateEnemies(Level level) throws FileNotFoundException{
        Random rand = new Random();
        Entity rotated;
        if(difficulty == 1){
            for(int i = 0; i < 20; i++){
                int x = rand.nextInt(gamewidth-(gamewidth/10))+((gamewidth/10)-100);
                int y = rand.nextInt(gameheight-(gameheight/10))+((gameheight/10)-100);
                int width = rand.nextInt(300)+50;
                int height = rand.nextInt(150)+50;
                level.createEntity(x, y, width, height,"Barricade");
            }
        }
        else if(difficulty == 2){
            for(int i = 0; i < 30; i++){
                int x = rand.nextInt(gamewidth-(gamewidth/10))+((gamewidth/10)-100);
                int y = rand.nextInt(gameheight-(gameheight/10))+((gameheight/10)-100);
                int width = rand.nextInt(300)+50;
                int height = rand.nextInt(150)+50;
                int yVel = rand.nextInt(90)+10;
                int enemyType = rand.nextInt(3);
                
                if(enemyType == 2){
                    level.createEntity(x, y, width, height, directory, 0, yVel);
                }
                else{
                    level.createEntity(x, y, width, height,"Barricade");
                }
            }
        }
        else if(difficulty == 3){
            for(int i = 0; i < 30; i++){
                int x = rand.nextInt(gamewidth-(gamewidth/10))+((gamewidth/10)-100);
                int y = rand.nextInt(gameheight-(gameheight/10))+((gameheight/10)-100);
                int width = rand.nextInt(300)+50;
                int height = rand.nextInt(150)+50;
                int yVel = rand.nextInt(90)+10;
                int enemyType = rand.nextInt(10);
                int rotate = rand.nextInt(8);
                if(enemyType%2 == 0){
                    if(rotate == 5){
                        rotated = level.createEntity(x, y, width, height, "Moving_Barricade", 0, yVel);
                        rotated.entityVisual.setRotate(rand.nextInt(360));
                    }
                    else
                        level.createEntity(x, y, width, height, "Moving_Barricade", 0, yVel);

                }
                else if(enemyType == 1 || enemyType == 3){
                    if(rotate == 5){
                        rotated = level.createEntity(x, y, width, height,"Barricade");
                        rotated.entityVisual.setRotate(rand.nextInt(360));
                    }
                    else
                        level.createEntity(x, y, width, height,"Barricade");  
                }
                else{
                    level.createEntity(x,y,width,height,"Enemy");
                }
            }
        }
    }
    //The standard gameloop
    private void gameLoop(Level level) throws FileNotFoundException{
        started = false;
        failed = false;
        Level currentLevel = level;
        scene.setRoot(currentLevel.getPane());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new keyHandler(currentLevel.camera, currentLevel.camera.name, this.currentPane));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new keyHandler(currentLevel.camera, currentLevel.camera.name, this.currentPane));
        scene.addEventHandler(KeyEvent.KEY_PRESSED, new keyHandler(currentLevel.camera.player, currentLevel.camera.player.name));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new keyHandler(currentLevel.camera.player,currentLevel.camera.player.name));
        stage.setScene(scene);
        stage.show();
        
        AnimationTimer animation = new AnimationTimer(){
            double firstTime;
            double lastTime;
            double currentTime;
            double elapsedTimeSeconds;
           
            @Override
            public void handle(long now){
                if(currentLevel.camera.getHealth() <= 0){
                    failed = true;
                }
                if(failed)
                {
                    this.stop();
                    currentPane.set(3);
                }
                if(!started){
                    firstTime = System.currentTimeMillis();
                    lastTime = firstTime;
                    started = true;
                    System.out.println("Program Started: "+firstTime);
                }
                currentTime = System.currentTimeMillis();
                //System.out.println("milis: "+currentTime);
                elapsedTimeSeconds = ((currentTime-lastTime)/100.0);
                //System.out.println(elapsedTimeSeconds);
                updateLocations(currentLevel, elapsedTimeSeconds);
                wallCollisions(currentLevel);
                EntityCollision(currentLevel);
                updateVisuals(currentLevel);
                lastTime = currentTime;
            }
        };
        currentPane.addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //System.out.println(currentPane);
                if(newValue.toString().equals("0")){
                    animation.stop();
                    scene.setRoot(getScreen(currentLevel));
                }
                if(newValue.toString().equals("1")){
                    started = false;
                    animation.start();
                    scene.setRoot(getScreen(currentLevel));
                }
                if(newValue.toString().equals("2")){
                    animation.stop();
                    System.out.println("Stopped");
                    scene.setRoot(getScreen(currentLevel));
                }
                if(newValue.toString().equals("3")){
                    animation.stop();
                    buildEndscreen(failed);
                }
            }
        });
        animation.start();
    }
    private void buildEndscreen(boolean failed){
        Label result;
        if(failed){
            result = new Label("Failed");
        }
        else{
            result = new Label("Success");
        }
        result.setLayoutX(gamewidth/2);
        result.setLayoutY(gameheight/2);
        Pane endpane = getScreen(null);
        endpane.getChildren().add(result);
        scene.setRoot(endpane);
    }
    //Updates the locations of all elements currently in the level.
    private void updateLocations(Level currentLevel, double time){
        for(int i = 0; i< currentLevel.entityList.size(); i++){
            currentLevel.entityList.get(i).updateLocation(time);
        }
        currentLevel.camera.updateLocation(time);
    }
    //Calculates reactions to objects, including the camera, colliding with a wall
    private void wallCollisions(Level currentLevel){
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i< currentLevel.entityList.size(); i++){
                boolean audio = collider.Entity_WallCollisions(currentLevel.entityList.get(i));
        }
        currentLevel.camera.CamerawallCollisions(this.gamewidth, this.gameheight);
        boolean audio = collider.Entity_WallCollisions(currentLevel.camera.player);
        /*if(audio){
            AudioClip hitSound = new AudioClip(""); 
            hitSound.play();
        }*/
    }
    //Handles inter-entity collisions
    private void EntityCollision(Level currentLevel){
        Collisions collider = new Collisions(this.gamewidth,this.gameheight);
        for(int i = 0; i< currentLevel.entityList.size(); i++){
            Entity currentEntity = currentLevel.entityList.get(i);
            boolean audio = collider.Entity_EntityCollisions(currentLevel.camera.player, currentEntity);
        }
        /*if(audio){
            AudioClip hitSound = new AudioClip(""); 
            hitSound.play();
        }*/
    }
    //updates the actual shown graphics after collisions have been calculated
    private void updateVisuals(Level currentLevel){
        for(int i = 0; i< currentLevel.entityList.size(); i++){
                currentLevel.entityList.get(i).updateVisual();
            }
        currentLevel.camera.updateVisual();
    }
}
