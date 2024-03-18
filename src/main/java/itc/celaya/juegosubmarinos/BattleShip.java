package itc.celaya.juegosubmarinos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BattleShip extends Application {
    @Override
    public void start(Stage stage){
        //Variable para asignar un tamaño de size*size de la matriz
        final int  size = 5;

        //Objecto que permite visualizar imagenes, utilizado para mostrar el resultado de victoria o derrota de la partida
        ImageView resultScreen = new ImageView();

        //Instancia de la clase Player, con un parametro size en el constructor para general la matriz
        Player player = new Player(size);

        //Instancia de la clase CPU, con parametros size, instancia player y resultScreen
        CPU machine = new CPU(size, player,resultScreen);
        //se utilizar a player para leer y modificar valores del objeto player
        //resultScreen se le asigna una imagen con un condicional contenido en la logica de la clase

        //Objeto de la clase XBox, el cual sirve como contenedor para almacenar todos los elementos graficos necesarios para el juego.
        VBox GUI = new VBox();
        GUI.getChildren().addAll(machine.GUI,player.GUI);

        //Objeto de la clase StackPane para poner encima la pantalla de victoria o derrota
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(GUI, resultScreen);

        Scene scene = new Scene(stackPane, 470,742);

        //libreria para asignar un estilo al VBox, unicamente para mantener el codigo mas limpio.
        StylesLibrary stylesLibrary = new StylesLibrary();
        stylesLibrary.setVBoxStageSyle(GUI);

        //Icono de la aplicacion
        Image icon = new Image("Logo ITC.png");
        stage.getIcons().add(icon);

        //titulo
        stage.setTitle("Battleship ITC! v1.0");

        //quitar propiedad de redimensionar la ventana
        //stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
//Clase padre de la cual las clases Player y CPU van a heredar, son todos los elementos graficos que las dos clases comporten.
class BattleShipsBoard{
    //Objeto grafico que permite almacenar elementos en una cuadricula, actua como una matriz por lo que es facil de organizar elementos en esta.
    GridPane boardObjects;
    //Matriz de tipo entero para ubicar las posiciones vacias y posiciones ocupadas de las naves en el tablero.
    int[][] boardShipMemory;
    //Objeto etiqueta que mostrara el dialogo de los personajes dependiendo del resultado de las acciones del tablero.
    Label dialogs;
    //Un matriz de objetos de tipo boton que representara graficamente las coordenadas posibles del tablero.
    Button[][] buttons;
    //Dos objeto etiqueta de texto, utilizada para mostrar las coordenadas de cada boton, puramente estitco.
    Label coordinatesX[];
    Label coordinatesY[];
    //Objeto etiqueta que nos ayudara a mostrar texto, en este caso el nombre del jugador.
    Label playerNameLabel;
    //Representa el numero de naves que cada jugador va a tener, representa el numero de naves que todavia no han sido destruidas.
    byte lives=5;
    //Un contenedor de tipo vertical, almacena todos los elementos graficos que conforman el juego, este es utilizado en el Main por una instanacia de la clase.
    VBox GUI;
    //Imagen del jugador.
    ImageView profilePicture = new ImageView();
    VBox playerProfile;

    BattleShipsBoard(int size, boolean flag, String color, String name){
        //Objeto de tipo StylesLibrary, utilizada para mayor limpieza en el codigo. Sirve para darle un estilo especifico a los elementos graficos.
        StylesLibrary stylesLibrary = new StylesLibrary();

        //Instanciamos la Matriz boardShipMemory para que tenga un lugar en memoria y sea utilizable.
        boardShipMemory = new int[size][size];

        //Variable para controlar el numero de naves en el tablero, en este caso se controla atravez del valor lives (vidas).
        int numberOfships = lives;
        //Se utilizar esta variable como variable de control para checar que se pusieron todas las naves deseadas en el tablero antes de poder terminar el siguiente ciclo mas adelante.

        //Proceso de inicializar los valores del tablero:
        //Doble ciclo for() para acceder a todos los elementos de la matriz boardShipMemory.
        for(int i = 0; i<size&&numberOfships!=0; i++){
            for(int k = 0; k<size&&numberOfships!=0; k++){
                //Este random genera numeros entre 1 y 0 pero de tipo decimal, es necesario entonces utilizar el metodo round the Math para que eliga uno u otro valor (1 o 0). (int) forza que de un resultado tipo entero.
                int random = (int) Math.round(Math.random());
                //Aqui definimos un condicional donde checamos el resultado del valor de random, y asignamos una nave al tablero en la coordenada i,k cuando random es igual a 1.
                if(random==1)
                {
                    boardShipMemory[i][k]=1;
                    numberOfships--;
                    k++;
                }
                else
                    boardShipMemory[i][k]=0;
            }
            //Esta es una medida de seguridad, forzamos a que se llene el tablero con las "n" naves antes de terminar el ciclo.
            //Si detecta que todavia faltan poner naves en el tablero va a reinicializar el ciclo for desde 0.
            if(i+1==size&&numberOfships!=0){
                i=0;
            }
        }
        //Siguientes instrucciones es instanciar los objetos atributos de diversas clases:
        playerNameLabel = new Label();
        dialogs = new Label();
        boardObjects = new GridPane();
        //Elemento grafico que permite mostrar imagenes, se utiliza para mostrar la imagen del jugador.
        ImageView profilePicture = new ImageView();
        //Un contenedor de tipo vertical, almacena lo relevante al jugador.
        playerProfile = new VBox();

        HBox hBox = new HBox(playerProfile, boardObjects);

        GUI = new VBox(hBox, dialogs);

        //Instancias las cuales ahora se les da un espacio en memoria concreto apartir de la variable size.
        buttons = new Button[size][size];
        coordinatesX = new Label[size];
        coordinatesY = new Label[size];

        //Ciclo para introducir los objetos lable en el girdPane, sirve para mostrar las coordenadas.
        for (int i = 0; i < size; i++) {
            coordinatesX[i] = new Label(Integer.toString(i));
            coordinatesY[i] = new Label(Integer.toString(i));

            boardObjects.add(coordinatesX[i], i+1, 0);
            boardObjects.add(coordinatesY[i], 0, i+1);

            stylesLibrary.setCoordinatesStyle(coordinatesX[i]);
            stylesLibrary.setCoordinatesStyle(coordinatesY[i]);
        }

        //Se hace uso de la clase StylesLibrary para darle formato a los elementos. Permite mas facilmente cambiar configuraciones.
        stylesLibrary.setNameStyle(playerNameLabel, name, color);
        stylesLibrary.setGridPaneStyle(boardObjects);
        stylesLibrary.SetVBoxStyle(playerProfile, color);
        stylesLibrary.setHBoxSyle(hBox,color);
        stylesLibrary.setDialogStyle(dialogs, color);
    }
}
//Clase que hereda de la clase BattleShipsBoard, inicializa los objetos que representan las naves y espacios vacios.
//Es utilizada principalmente por la clase CPU para la logica del juego
class Player extends BattleShipsBoard {
    //Atributo que almacena los intentos de la CPU al tablero del jugador.
    int[][] memory;
    Player(int size){
        super(size,true, "0093ff", "PLAYER");

        profilePicture.setImage(new Image(getClass().getResource("/Render Loop.gif").toExternalForm()));
        playerProfile.getChildren().addAll(profilePicture, playerNameLabel);
        profilePicture.setFitHeight(100);
        profilePicture.setFitWidth(100);

        dialogs.setText("LINCE: \"Hola, ¡Vamos a derrotar a esa chatarra!\"");
        memory = new int[size][size];
        for(int i = 0; i<size; i++){
            for(int k = 0; k<size; k++){
                memory[i][k]=0;
                buttons[i][k] = customButton(i,k, "0093ff", boardShipMemory[i][k]);
                boardObjects.add(buttons[i][k], i+1, k+1);
            }
        }
    }
    //Es un metodo para la creacion de un boton con cierto estilo y logico.
    //La logica del boton aqui unicamente es puramente estitica, dado que el usuario no puede atacar a sus propias naves.
    Button customButton(int i, int k, String color, int ship){
        //De nuevo la libreria StylesLibrary para tener un codigo mas limpio
        StylesLibrary styles = new StylesLibrary();

        //instancio un objeto de tipo Button el cual va ser utilizado en el return de la clase.
        Button btn = new Button();

        //Objetos que almacenan las imagenes necesarias para los botones en su inicializacion.
        //Acceden a la carpeta src resources.

        //Objetos que permiten visualizar las imagenes.
        ImageView imageQuestionMark = new ImageView();
        ImageView imageShip = new ImageView();

        imageQuestionMark.setImage(new Image(getClass().getResource("/questionmark.png").toExternalForm()));
        imageShip.setImage(new Image(getClass().getResource("/ship.png").toExternalForm()));


        //Checa si existe una nave utilizando el parametro ship, el valor de la matriz de este atributo se inicializo en la clase padre como 0s y 1s para representas espacios vacios y naves respectivamente.
        //ship es una referencia al atributo hererado boardShipMemory[i][k]
        if(ship==0)
            btn.setGraphic(imageQuestionMark);
        else
            btn.setGraphic(imageShip);
        //Asigna una imagen con signo de interrogacion a espacios con 0s y una nave a espacios con 1s.

        //Expresion de tipo lamba, crea variables de tipo final, razon por la cual los valores no se pierden.
        //Almacena las coordenadas i,k, puede hacer referencia a boardShipMemory[x][y] para checar si existe nave o no en el lugar seleccionado con el mouse.
        btn.setOnAction(event -> {
            int x = i, y=k;
            if(boardShipMemory[i][k]==1){
                btn.setStyle(styles.getButtonnColor(color));
                //Puro flavor text (dialogo para hacer parecer al programa mas vivo).
                dialogs.setText("LINCE: N-no voy a disparar a mi propio barco!");
            }
            else{
                //Puro flavor text (dialogo para hacer parecer al programa mas vivo).
                dialogs.setText("LINCE: Ahi no tengo ningun barco, esperemos que seleccione ese :)");
            }
        });

        //Pura vanidad.
        btn.setOnMouseEntered(event -> {
            btn.setOpacity(1);
            btn.setStyle(styles.getHoverColor());
        });

        //Pura vanidad.
        btn.setOnMouseExited(event -> {
            btn.setOpacity(0.9);
            btn.setStyle(styles.getButtonnColor(color));
        });

        //Asigno un tamaño estandar a los botones, es importante dado que la imagen de los jugadores la visualice como el doble del tamaño de un boton
        btn.setMinSize(50,50);
        btn.setMaxSize(50,50);

        //Pura vanidad, una transparencia porque originalmente el programa tenia un fondo de mar, pero ultimamente no se utilizo, se veia feo.
        btn.setOpacity(0.9);
        //Asignar estilo al boton.
        btn.setStyle(styles.getButtonnColor(color));
        return btn;
    }
}
//Clase que hereda de la clase BattleShipsBoard;
//Contrario a lo que pareceria el usuario unicamente tiene control sobre esta clase.
//Se utiliza un atributo de tipo Player como nodo/refencia a la instancia principal de la clase Player, esto porque despues de tu accion en la instancia de la clase CPU, esta debe modificar valores de la instancia player.
class CPU extends BattleShipsBoard {
    Player opponent;
    CPU(int size, Player opponent,ImageView resultScreen){
        //Llamo al contructor de la clase BattleShipsBoard y le introduzco parametros necesarios para que inicialice sus atributos.
        //Pude haberlo mejorado un poco los parametros, por ejemplo la bandera se utiliza para checar si es player o cpu apesar de que utilice un String name para manualmente asignarle el nombre al jugador.
        //Si tengo tiempo hay que mejorar esto:
        //Ademas de que asigno un color especifico en el parametro String color, pero utilizo una libreria que se encargar de los colores, otravez, se puede mejorar eso.
        super(size,false, "cb0b17", "CPU");

        profilePicture.setImage(new Image(getClass().getResource("/cpu.gif").toExternalForm()));
        playerProfile.getChildren().addAll(profilePicture, playerNameLabel);
        profilePicture.setFitHeight(100);
        profilePicture.setFitWidth(100);

        //Asigno el valor del atributo opponent igual a la direccion de la instancia principal player de la clase Player.
        this.opponent = opponent;

        playerNameLabel.setText("CPU");
        dialogs.setText("T-1000: \"BEEP BOOP. PREPARATE. PARA. SER. ANIQUILADO.\"");

        //Instancio los botones, y los introduzco al gridpane, no hay mucho que decir ademas de que toda la logica del programa basicamente recide en el metodo button.
        for(int i = 0; i<size; i++){
            for(int k = 0; k<size; k++){
                buttons[i][k] = customButton(i,k,"cb0b17",resultScreen);
                boardObjects.add(buttons[i][k], i+1, k+1);
            }
        }
    }

    Button customButton(int coodinateX, int coodinateY, String color, ImageView resultScreen){
        //Lo mismo que en la clase Player, instanciar objetos, inicializar valores.
        StylesLibrary styles = new StylesLibrary();
        Button btn = new Button();

        ImageView imageQuestionMark = new ImageView();
        ImageView imageRocket = new ImageView();
        ImageView imageHit = new ImageView();
        ImageView imageMiss = new ImageView();
        ImageView imageHitPlayer = new ImageView();
        ImageView imageMissPlayer = new ImageView();

        imageQuestionMark.setImage(new Image(getClass().getResource("/questionmarkmist.png").toExternalForm()));
        imageRocket.setImage(new Image(getClass().getResource("/rocket.png").toExternalForm()));
        imageHit.setImage(new Image(getClass().getResource("/hit.png").toExternalForm()));
        imageMiss.setImage(new Image(getClass().getResource("/miss.png").toExternalForm()));
        imageHitPlayer.setImage(new Image(getClass().getResource("/hit.png").toExternalForm()));
        imageMissPlayer.setImage(new Image(getClass().getResource("/miss.png").toExternalForm()));

        //Le asigno un grafico a los botones, como todas las coordenadas estan vacias entonces todas deben tener signo de interrogacion por default.
        btn.setGraphic(imageQuestionMark);

        //Estilo del boton
        btn.setStyle(styles.getButtonnColor(color));

        btn.setOnAction(event -> {
            //Coordenadas almacenadas con lo parametros de los ciclos for
            //Concuerdan con la matriz de tipo entera boardShipMemory
            int x = coodinateX, y=coodinateY;

            //Llama al valor de boardShipMemory[coodinateX][coodinateY] y revisa si tiene una nave (1).
            if(boardShipMemory[coodinateX][coodinateY]==1){
                //si existe una nave, significa que destruiste una nave enemiga, por lo tanto lives se vuelve lives -1
                lives--;
                //Como la casilla ya fue utilizada se tiene que desactivar el boton.
                btn.setDisable(true);
                //Texto inmersivo
                dialogs.setText("T-1000: \"HAZ. TENIDO. SUERTE. ESTA. VEZ. GATO. 0100101010101010\"");
                //Se le cambia el grafico al boton por el de una nave destruida.
                btn.setGraphic(imageHit);
            }
            else{
                //Como la casilla ya fue utilizada se tiene que desactivar el boton.
                btn.setDisable(true);
                //le bajo la opacidad por que como ya fue utilizada ya no es necesaria verla, ademas de que fue un fallo, no hay que hacer sentir mal al jugador fue mi logica.
                btn.setOpacity(0.5);
                //Texto burlon, falleste al darle a una nave.
                dialogs.setText("T-1000: \"HAHAHA FALLASTE GATO, AHORA ES MI TURNO. BEEP BOOP.\"");
                btn.setGraphic(imageMiss);
            }
            //bandera para checar si ya selecciono una casilla la cpu
            boolean flag=true;

            //Ciclos for para seleccionar una casilla al azar del boardShipMemory
            for(int i = 0; i< boardShipMemory.length&&flag; i++){
                for(int k = 0; k< boardShipMemory.length&&flag; k++){
                    //se genera un numero aleatorio 0 o 1, y si es 1 significa que la cpu eligio la casilla actual como objetivo
                    int random = (int) Math.round(Math.random());
                    if(random==1){
                        //esto sirve garantizar que no se a seleccionado esta ubicacion antes, de esta manera la cpu no puede seleccionar una coordenada dos veces.
                        if(opponent.memory[i][k]==0){
                            opponent.memory[i][k]=1;
                            //la cpu selecciona una posicion aleatoria y despues checa si existe una nave en esa posicion seleccionada en boardShipMemory de player
                            if(opponent.boardShipMemory[i][k]==1){
                                //encontro una nave, le reduce la vida
                                opponent.lives--;
                                opponent.dialogs.setText("LINCE: \"NOOOOOOOOOO.\"");
                                //se le asigna una nave destruida a la posicion seleccionada en player
                                opponent.buttons[i][k].setGraphic(imageHitPlayer);
                            }
                            else{
                                //la cpu selecciona una posicion aleatoria y fallo.
                                opponent.buttons[i][k].setOpacity(0.5);
                                opponent.dialogs.setText("LINCE: \"Fallaste robot!\"");
                                //le asigna al boton player una imagen de falla
                                opponent.buttons[i][k].setGraphic(imageMissPlayer);
                            }
                            //esta bandera permite terminar el ciclo, indicando que la cpu logro elegir una casilla vacia exitosamente
                            flag = false;
                        }
                        //si la posicion actual ya fue seleccionada simplemete se salta la casilla
                    }
                }
                if(i+1== boardShipMemory.length&&flag)
                    i=0;
            }
            //al final de que los dos jugadores seleccionaran las casillas checo si alguna de las vidas de los dos es igual a 0
            if(lives==0||opponent.lives==0){
                //EMPATE
                if(lives==0&&opponent.lives==0){
                    dialogs.setText("T-1000: \"EMPATE!?!??!?!\"");
                    opponent.dialogs.setText("LINCE: \"EMPATE!?!??!?!\"");
                }
                //PIERDE LA CPU
                else  if(lives==0)
                {
                    dialogs.setText("T-1000: \"NOOOOOOOOOOOOOOO. PERDIIIIIIIIIIII!!!!\"");
                    opponent.dialogs.setText("LINCE: \"GAAAAANEEEEEEEE SIIIIIIIIIII!!!!!!\"");
                    resultScreen.setImage(new Image("victory.png"));

                }
                //PIERDE EL LINCE
                else {
                    dialogs.setText("T-1000: \"VICTORIA. TOTAL. PARA. LAS. MAQUINAS. BEEP BOOP.\"");
                    opponent.dialogs.setText("LINCE: \"perdi... noooo.....\"");
                    resultScreen.setImage(new Image("defeat.png"));

                }
                //como el juego ya se acabo deshabilito los botones:
                for(int i = 0; i< boardShipMemory.length; i++) {
                    for (int k = 0; k < boardShipMemory.length; k++) {
                        buttons[i][k].setDisable(true);
                        opponent.buttons[i][k].setDisable(true);
                    }
                }
            }

        });

        btn.setOnMouseEntered(event -> {
            if (btn.isDisabled());
            else{
                btn.setOpacity(1);
                btn.setGraphic(imageRocket);
                btn.setStyle(styles.getHoverColor());
            }
        });

        btn.setOnMouseExited(event -> {
            if (btn.isDisabled());
            else{
                btn.setGraphic(imageQuestionMark);
                btn.setStyle(styles.getButtonnColor(color));
            }
        });

        btn.setMinSize(50,50);
        btn.setMaxSize(50,50);

        btn.setOpacity(0.92);
        return btn;
    }
}
class StylesLibrary {
    private String btnStyle_1 = "-fx-border-color: #";
    private String btnStyle_2 = "; -fx-border-width: 1px; -fx-border-radius: 0; -fx-background-color: #313131;";
    private String btnHoverStyle = "-fx-background-color: #4c4c4c;-fx-background-radius: 0; -fx-border-width: 1px;  -fx-border-color: #0093ff";
    public void setVBoxStageSyle(VBox vBox){
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(12,10,0,10));
        vBox.setStyle("-fx-background-color: #272727;-fx-background-radius: 0; -fx-border-width: 1px;  -fx-border-color: #2d2d2d");
    }
    public String getButtonnColor(String color){return btnStyle_1 + color + btnStyle_2;}
    public String getHoverColor(){return btnHoverStyle;}

    public void setCoordinatesStyle(Label coordinates){
        coordinates.setStyle("-fx-background-color: #2d2d2d; -fx-border-color: #303030; -fx-border-width: 2px;");
        coordinates.setTextFill(Color.web("GRAY"));
        coordinates.setAlignment(Pos.CENTER);
        coordinates.setMinSize(50,50);
    }
    public void setHBoxSyle(HBox hBox, String color){
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.setStyle("-fx-background-color: #252525; -fx-border-color: #"+ color + "; -fx-border-width: 1px;");
    }

    public void SetVBoxStyle(VBox vBox, String color){
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(10,10,0,10));
        vBox.setStyle("-fx-background-color: #2d2d2d;-fx-border-color: #"+ color + "; -fx-border-width: 0.5px;");
    }

    public void setNameStyle(Label label, String name, String color){
        label.setStyle("-fx-background-color: #1a1a1a; -fx-border-color: #"+ color + "; -fx-border-width: 2px; ");
        label.setTextFill(Color.web("#"+color));
        label.setPadding(new Insets(5,0,5,0));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setText(name);
        label.setOpacity(0.9);
        HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
    }

    public void setDialogStyle(Label label, String color){
        label.setStyle("-fx-background-color: #232323; ");
        label.setTextFill(Color.web("#"+color));
        label.setPadding(new Insets(5,0,5,0));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.setOpacity(0.9);
        HBox.setHgrow(label, javafx.scene.layout.Priority.ALWAYS);
    }

    public void setGridPaneStyle(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        gridPane.setPadding(new Insets(10));
    }
}