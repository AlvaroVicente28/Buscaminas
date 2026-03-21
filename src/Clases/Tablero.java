package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Properties;

import Clases.Juego.Resultado;

public class Tablero {
  private static final int MINAS_FACIL = 10;
  private static final int MINAS_NORMAL = 40;
  private static final int MINAS_DIFICIL = 99;

  private static final int TAMANO_FACIL = 9;
  private static final int TAMANO_NORMAL = 14;
  private static final int TAMANO_DIFICIL = 20;

  private static final int CELDA_FACIL = 67;
  private static final int CELDA_NORMAL = 43;
  private static final int CELDA_DIFICIL = 30;

  private static final Color VERDE_CLARITO = new Color(93, 209, 73);
  private static final Color VERDE_OSCURO = new Color(61, 167, 42);
  private static final Color VERDE_PANEL_SUPERIOR = new Color(23, 94, 48);
  private static final Color VERDE_PASTEL_CLARO = new Color(198, 241, 213);
  private static final Color VERDE_PASTEL_OSCURO = new Color(33, 131, 67);
  private static final Color MARRON_CLARITO = new Color(229, 194, 159);
  private static final Color MARRON_OSCURO = new Color(215, 184, 153);
  private static final Color CIAN = new Color(159, 159, 229);
  private static final Color VERDE = new Color(73, 209, 121);
  private static final Color ROJO = new Color(209, 73, 93);
  private static final Color MORADO = new Color(121, 73, 209);
  private static final Color AZUL = new Color(73, 93, 209);

  private JFrame ventana;
  private JPanel panelBanderillas;
  private JPanel panelSuperior; 
  private JPanel leftPanel;
  private JButton [][] botones;
  private JLabel etiquetaBanderillas;
  private JLabel imagen1;
  private JLabel imagen2;
  private JLabel labelTimer;
  private JComboBox<String> reinicio;
  private Juego juego;
  private Timer timer;
  private Dificultad dificultad;
  private int minas; 
  private int segundos;
  private HashMap<String, ImageIcon> cacheImagenes = new HashMap<>();
  private static final String[] rutas = {
    "/Imagenes/Cero.png", 
    "/Imagenes/Uno.png", 
    "/Imagenes/Dos.png", 
    "/Imagenes/Tres.png", 
    "/Imagenes/Cuatro.png", 
    "/Imagenes/Cinco.png", 
    "/Imagenes/Seis.png", 
    "/Imagenes/Siete.png", 
    "/Imagenes/Ocho.png", 
    "/Imagenes/Nueve.png"
  };

  /**
   * Inicializa la selección de dificultad
   */
  public Tablero () {
    //Inicializacion del boton de reinicio
    reinicio = new JComboBox<>(new String[]{"REINICIAR", "FÁCIL", "NORMAL", "DIFÍCIL"});
    reinicio.setPreferredSize(new Dimension(145, 40));
    reinicio.setFont(new Font("Rockwell", Font.BOLD, 20));
    reinicio.setFocusable(false);
    reinicio.setOpaque(true);
    reinicio.getComponent(0).setBackground(VERDE_PASTEL_OSCURO);
    
    reinicio.setBorder(BorderFactory.createLineBorder(VERDE_PANEL_SUPERIOR, 2));
    reinicio.setBackground(VERDE_PASTEL_OSCURO);
    reinicio.setForeground(Color.WHITE);
    reinicio.addActionListener(e -> {
      switch (reinicio.getSelectedIndex()){
        case 1: inicializar(Dificultad.FACIL); break;
        case 2: inicializar(Dificultad.NORMAL); break;
        case 3: inicializar(Dificultad.DIFICIL); break;
      }; 
      reinicio.setSelectedIndex(0);
    });
    //Crea un render para que el texto del JComboBox esté centrado
    DefaultListCellRenderer renderer = new DefaultListCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    reinicio.setRenderer(renderer);

    //Inicialicación del JPanel de la pantalla de inicio
    JPanel pantalla = new JPanel(new GridBagLayout());

    //Botones de dificultad
    JButton facil = botonesDificultad(VERDE_CLARITO, "FÁCIL");
    JButton normal = botonesDificultad(AZUL, "NORMAL");
    JButton dificil = botonesDificultad(ROJO, "DIFÍCIL");
    //ActionsListners
    facil.addActionListener(e -> inicializar(Dificultad.FACIL));
    normal.addActionListener(e -> inicializar(Dificultad.NORMAL));
    dificil.addActionListener(e -> inicializar(Dificultad.DIFICIL));
    //Padding a los botones
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 10, 0, 10); 
    //Se añaden los botones con el padding
    pantalla.add(facil, gbc);
    pantalla.add(normal, gbc);
    pantalla.add(dificil, gbc);

    //Inicializacion de la ventana
    ventana = new JFrame("Buscaminas");        
    ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    ventana.setLayout(new BorderLayout());  
    ventana.setResizable(false); 
    ventana.setSize(1080, 720);
    ventana.setLocationRelativeTo(null); //OBS: si se llama despues de darle tamaño se centra
    //Icono de la ventana
    java.net.URL iconURL = getClass().getResource("/Imagenes/Buscaminas_Icono.png");
    if (iconURL == null) {
      System.err.println("Couldn't find file: /Imagenes/Buscaminas_Icono.png");
    }
    else {
      ventana.setIconImage(new ImageIcon(iconURL).getImage());
    }
    //Mensaje de selector de dificultad
    JLabel mensaje = new JLabel("", JLabel.CENTER);
    mensaje.setFont(new Font("Javanese Text", Font.BOLD, 25));
    mensaje.setText("Seleccione la dificultad");
    mensaje.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

    //Se añade todo a la ventana
    ventana.add(mensaje, BorderLayout.NORTH);
    ventana.add(pantalla, BorderLayout.CENTER);   
    ventana.revalidate();  //Actualiza la ventana
    ventana.setVisible(true);
  }

  /**
   * Funcion auxiliar para crear los botones de las dificultades
   * @param color {@code Color} del botón
   * @param txt texto del botón
   * @return el nuevo {@code JButton}
   */
  private JButton botonesDificultad(Color color, String txt) {
    JButton boton = new JButton(txt);
    boton.setBackground(color);
    boton.setFont(new Font("Rockwell", Font.BOLD, 16));
    boton.setForeground(Color.BLACK);
    boton.setPreferredSize(new Dimension(120, 30));
    boton.setFocusable(false); //Deshabilitar el enfoque
    return boton;
  }

  /**
   * Se inicializa el juego
   * @param opcion dificultad seleccionada
   */
  private void inicializar(Dificultad opcion) {
    ventana.getContentPane().removeAll(); //Limpio la ventana
    if (timer != null){
      timer.stop();
    }
    dificultad = opcion;
    int tamano = 0, tamanoCasilla = 0;
    switch (opcion){            //Tamaños y minas para cada dificultad
      case FACIL: minas = MINAS_FACIL; tamano = TAMANO_FACIL; tamanoCasilla = CELDA_FACIL; break;
      case NORMAL: minas = MINAS_NORMAL; tamano = TAMANO_NORMAL; tamanoCasilla = CELDA_NORMAL; break;
      case DIFICIL: minas = MINAS_DIFICIL; tamano = TAMANO_DIFICIL; tamanoCasilla = CELDA_DIFICIL; break;
    }
    int dimension = tamano * tamanoCasilla;
    juego = new Juego(tamano, minas);

    //Elementos de la escena
    JPanel panelPrincipal = new JPanel(new GridLayout(tamano, tamano));//Botones (centro) 
    JPanel contenedor = new JPanel(new GridBagLayout()) {//Contenedor auxiliar para panel y que respeten las casillas su tamaño
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint degradado = new GradientPaint(
          0, 0, VERDE_PASTEL_CLARO,              // esquina superior izquierda
          getWidth(), getHeight(), VERDE_PASTEL_OSCURO  // esquina inferior derecha
        );
        g2d.setPaint(degradado);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };
    botones = new JButton[tamano][tamano]; //Array con los botones (casillas)

    panelSuperior = new JPanel(new BorderLayout()); 
    etiquetaBanderillas = new JLabel("", JLabel.LEFT);
    imagen1 = new JLabel();
    imagen2 = new JLabel();
    panelBanderillas = new JPanel(new GridBagLayout());
    labelTimer = new JLabel("00:00", JLabel.CENTER);
    leftPanel = new JPanel(new GridBagLayout());

    panelPrincipal.setPreferredSize(new Dimension(dimension, dimension));

    contenedor.add(panelPrincipal);

    panelSuperior.setBackground(VERDE_PANEL_SUPERIOR);
    panelSuperior.add(leftPanel, BorderLayout.WEST);
    panelSuperior.add(panelBanderillas, BorderLayout.EAST);
    panelSuperior.add(labelTimer, BorderLayout.CENTER);

    etiquetaBanderillas.setPreferredSize(new Dimension(60, 60));
    etiquetaBanderillas.setIcon(getImageIcon("/Imagenes/Banderilla.png", 50, 50));

    imagen1.setPreferredSize(new Dimension(60, 60));
    imagen2.setPreferredSize(new Dimension(60, 60));

    panelBanderillas.setBackground(VERDE_PANEL_SUPERIOR);
    panelBanderillas.add(etiquetaBanderillas);
    panelBanderillas.add(imagen1);
    panelBanderillas.add(imagen2);

    labelTimer.setFont(new Font("Bauhaus 93", Font.BOLD, 32));
    labelTimer.setForeground(Color.WHITE);
    labelTimer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); //Para que quede centrado verticalmente

    leftPanel.add(reinicio);
    leftPanel.setBackground(VERDE_PANEL_SUPERIOR);
    leftPanel.setPreferredSize(panelBanderillas.getPreferredSize()); //Pilla el mismo tamaño para que sea simetrico

    numBanderillas();

    //Inicializo todas las casillas (botones)
    for (int i = 0; i < tamano; i++){
      for (int j = 0; j < tamano; j++){
        final int fila = i, column = j;    //Necesario que sean final para usar el listener
        JButton boton = botones[i][j] = new JButton();
        boton.setBackground((i + j) % 2 == 0 ? VERDE_CLARITO : VERDE_OSCURO); //Color de las casillas
        boton.setFocusable(false); 
        boton.setMargin(new Insets(0, 0, 0, 0)); //El boton no tine margenes, ocupa toda la casilla
        boton.addMouseListener(click(fila, column));
        boton.setBorderPainted(false); //Le quito el borde para que no parezca un botón
        boton.setFont(new Font("Dialog", Font.BOLD, 20));
        panelPrincipal.add(boton);
      }
    }

    //Añadir todo a la ventana
    ventana.add(panelSuperior, BorderLayout.NORTH);
    ventana.add(contenedor, BorderLayout.CENTER);
    ventana.revalidate();           //Actualiza la ventana
  }

  /**
   * Funcion auxiliar para distinguir entre click derecho o izquierdo
   * @param x coordenada x
   * @param y coordenada y
   * @return el {@code MouseAdapter} que gestiona las acciones de click del usuario
   */
  private MouseAdapter click (int x, int y){
    return new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {//Se activa cuando se suelta el boton
        if (e.getComponent().contains(e.getPoint())) { //Comprueba que el click se ha hecho dentro del boton del que se ha iniciado
          if (SwingUtilities.isLeftMouseButton(e)){ 
            manejarClickDesp(x, y);
          }
          else if (SwingUtilities.isRightMouseButton(e)){
            manejarClickBanderilla(x, y);
          }
        }
      }
    };  
  }

  /**
   * Manejo del click en modo despejar casilla
   * @param x coordenada x
   * @param y coordenada y
   */
  private void manejarClickDesp(int x, int y){
    boolean primerClick = juego.getPrimerClick();
    Resultado res = juego.clickDesp(x, y);  //OBS: si res = BANDERILLA no se hace nada 
    if (res == Resultado.SEGUIR){ //Caso normal
      Casilla casilla = juego.getCasilla(x, y); 
      String num = casilla.toString();
      despejarCasilla(x, y, num);
    }
    else if (res == Resultado.HUECO || res == Resultado.DERROTA){ //Caso click en hueco o click en mina
      if (primerClick){
        iniciarTimer();
      }
      despejarHuecos(res);
    }
    else if (res == Resultado.VICTORIA) { //Caso victoria
      despejarHuecos(res);
      finPartida(true);
    }
  }

  /**
   * Manejo del click en modo poner banderilla
   * @param x coordenada x
   * @param y coordenada y
   */
  private void manejarClickBanderilla(int x, int y){ 
    Casilla casilla = juego.getCasilla(x, y); 
    if (minas > 0 || casilla.getBanderilla()) {
      int variacion = juego.clickBanderilla(x, y);   //Actualizo el numero de banderillas
      if (variacion != 0){
        minas += variacion;
        String str = casilla.getBanderilla() ? "🚩" : "";
        botones[x][y].setText(str);
        botones[x][y].setForeground(addColorText(str));
        numBanderillas();
      }
    }
  }

  /**
   * Despeja la {@code Casilla} (x,y) y le cambia el {@code Color} y el número a su botón
   * @param x cordenada x
   * @param y coordenada y
   * @param num número de la {@code Casilla} a poner en el botón
   */
  private void despejarCasilla (int x, int y, String num){
    JButton boton = botones[x][y];
    boton.setText(num);
    for (MouseListener ml : boton.getMouseListeners()) { 
      boton.removeMouseListener(ml); //Elimino todos los MouseListners para que esté disabled pero teniendo colores de texto
    }
    boton.setBackground(getColor(x, y));
    boton.setForeground(addColorText(num));
  }

  /**
   * Recorre la lista de {@code Casilla} modificadas en un hueco
   * @param opcion {@code DERROTA}, {@code HUECO} o {@code VICTORIA}
   */
  private void despejarHuecos(Resultado opcion) { 
    for(Point punto : juego.getList()){
      Casilla casilla = juego.getCasilla(punto.x, punto.y);
      if (casilla.getBanderilla()) {
        casilla.setBanderilla();  
        if (opcion == Resultado.HUECO || opcion == Resultado.VICTORIA){
          minas++;
        }
        else if (casilla.getHayMina()){
          casilla.setBanderilla(); //Se quedan marcadas las banderas correctas
        }
      }
      despejarCasilla(punto.x, punto.y, casilla.toString());            
    }
    numBanderillas();
    if (opcion == Resultado.DERROTA) { //Caso en el que se ha perdido
      finPartida(false);
    }
  }

  /**
   * Da {@code Color} a las casillas despejadas
   * @param x coordenada x
   * @param y coordenada y
   * @return {@code Color} de la casilla (x,y)
   */
  private Color getColor (int x, int y) { 
    Casilla casilla = juego.getCasilla(x, y);
    if (casilla.getHayMina() || casilla.getBanderilla()) {
      return (x + y) % 2 == 0 ? VERDE_CLARITO : VERDE_OSCURO;
    }
    return (x + y) % 2 == 0 ? MARRON_CLARITO : MARRON_OSCURO;
  }

  /**
   * Cambia el color del texto de la casilla segun el número
   * @param num número del botón
   * @return {@code Color} a poner
   */
  private Color addColorText (String num){
    switch (num){
      case "1": return CIAN;
      case "2": return VERDE;
      case "❌": 
      case "🚩":
      case "3": return ROJO;
      case "4": return MORADO;
      case "5": return Color.YELLOW;
      case "6": return AZUL;
      default: return Color.BLACK;
    }
  }

  /**
   * Inicia el cronometro de la partida
   */
  private void iniciarTimer(){
    segundos = 0;
    timer = new Timer(1000, e -> { // Cada 1 segundo
      segundos++;
      actualizarTimer();
    });
    timer.start();
  }

  /**
   * Pone el tiempo actual formateado
   */
  private void actualizarTimer(){
    int min = segundos / 60;
    int seg = segundos % 60;
    labelTimer.setText(String.format("%02d:%02d", min, seg));
  }

  /**
   * Pone las imagenes que corresponda con el numero de banderillas restantes
   */
  private void numBanderillas(){ 
    int decenas = minas / 10, unidades = minas % 10;
    //Primer numero
    imagen1.setIcon(getImageIcon(rutas[decenas], 50, 50)); 
    //Segundo numero
    imagen2.setIcon(getImageIcon(rutas[unidades], 50, 50)); 
  }

  /**
   * Devuelve una imagen redimensionada dada la ubiación
   * @param path ruta a la imagen
   * @param width anchura
   * @param height altura
   * @return {@code ImageIcon} si se encuentra la imagen, si no {@code null}
   */
  private ImageIcon getImageIcon(String path, int width, int height) {
    if (path == null) { 
      return null;
    }
    if (cacheImagenes.containsKey(path)) {
      return cacheImagenes.get(path);
    }
    java.net.URL imgURL = Tablero.class.getResource(path);
    if (imgURL == null) {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
    ImageIcon original = new ImageIcon(imgURL);
    BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = scaled.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(original.getImage(), 0, 0, width, height, null);
    g2d.dispose();

    ImageIcon resultado = new ImageIcon(scaled);
    cacheImagenes.put(path, resultado);
    return resultado;
  }

  /**
   * Crea una mini ventana de fin al terminar la partida
   * @param fin {@code true} si se ha gando la partida, {@code false} si se ha perdido
   */
  private void finPartida(boolean fin) {
    //Se deja de contar los segundos
    timer.stop();

    String recordActual = getRecord(fin);
    String msg = (fin) ? "¡Has ganado!" : "Has perdido...";
    JDialog dialog = new JDialog(ventana, "Fin de la partida", true);
    JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    JPanel tiempos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    JButton reiniciar = new JButton("Reiniciar");
    JButton salir = new JButton("Salir");
    JLabel mensaje = new JLabel(msg, JLabel.CENTER);
    JLabel record = new JLabel("00:00", JLabel.CENTER);

    record.setText(recordActual);
    if (!fin) {//Caso en el que se ha perdido
      labelTimer.setText("___"); //Si se pierde, el tiempo sale "___"
    }

    labelTimer.setForeground(Color.BLACK);
    record.setFont(new Font("Bauhaus 93", Font.BOLD, 32));
    record.setForeground(Color.BLACK);
    record.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); //Para que quede centrado verticalmente

    tiempos.add(labelTimer);
    tiempos.add(record);

    mensaje.setText(msg);
    mensaje.setFont(new Font("Dialog", Font.BOLD, 18));
    mensaje.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    reiniciar.setFocusable(false);
    salir.setFocusable(false);
    salir.setPreferredSize(reiniciar.getPreferredSize());

    salir.addActionListener(e -> {dialog.dispose(); ventana.dispose();});
    reiniciar.addActionListener(e -> {dialog.dispose(); reinicio();});
    
    botonesPanel.add(reiniciar);
    botonesPanel.add(salir);
    botonesPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.add(mensaje, BorderLayout.NORTH);
    dialog.add(tiempos, BorderLayout.CENTER);
    dialog.add(botonesPanel, BorderLayout.SOUTH);
    dialog.setSize(300, 200);
    dialog.setLocationRelativeTo(ventana);
    dialog.setResizable(false);
    dialog.setVisible(true);
  }

  /**
   * Reinicia la partida
   */
  private void reinicio() {
    ventana.getContentPane().removeAll(); 
    ventana.getContentPane().setBackground(null);
    inicializar(dificultad);
  }

  /**
   * Comprueba cual es el record y si se ha superado. Si se ha superado, tambien modifica o crea el archivo "records.txt"
   * @param ganado {@code true} si se ha gando la partida, {@code false} si se ha perdido
   * @return un {@code String} para que lo muestre el {@code JLabel} record
   */
  private String getRecord (boolean ganado){
    Properties records = new Properties();
    File archivo = new File("records.txt");
    boolean hayAntiguoRecord;
    if (archivo.exists()) {
      try (FileReader fr = new FileReader(archivo)) {
        records.load(fr);
      } 
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    String antiguo = records.getProperty(dificultad.name(), "00:00"); // Si no existe, devuelve "00:00" por defecto
    if (!esTiempoValido(antiguo)){
      antiguo = "00:00";
    }
    hayAntiguoRecord = !antiguo.equals("00:00");

    if (!ganado && !hayAntiguoRecord){
      antiguo = "___";
    }
    else {
      if (ganado && (antiguo.compareTo(labelTimer.getText()) > 0 || !hayAntiguoRecord)){
        records.setProperty(dificultad.name(), labelTimer.getText());
        antiguo = labelTimer.getText();
        try(FileWriter fw = new FileWriter(archivo)){
          records.store(fw, null);
        }
        catch (IOException e){
          e.printStackTrace();
        }
      }
    }
    return antiguo;
  }

  /**
   * Comprueba si el record tiene un formato adecuado. El formato es dd:ss {@code Integer} no negativos y con ss < 60
   * @param record {@code String} a comprobar
   * @return {@code true} si es valido, {@code false} e.o.c
   */
  private boolean esTiempoValido (String record){
    if (!record.matches("\\d{2}:\\d{2}")) {
      return false;
    }
    String[] partes = record.split(":");
    return Integer.parseInt(partes[1]) < 60; 
  }
  
  public enum Dificultad{
    FACIL,
    NORMAL,
    DIFICIL
  }
}