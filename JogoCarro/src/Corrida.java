import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JOptionPane;

public class Corrida extends Applet implements Runnable, KeyListener,
		MouseListener {

	private static final long serialVersionUID = 1L;

	// the main thread becomes the game loop
	Thread gameloop;

	// use this as a double buffer
	BufferedImage backbuffer;

	// the main drawing object for the back buffer
	Graphics2D g2d;

	// Creating image of life

	Path path = Paths.get("C:/Documents and Settings/LMC3/Desktop/JogoCarro/ranking.txt");
	Charset utf8 = StandardCharsets.UTF_8;
	String nomePlayer;
	String nomePlayer1;
	String nomePlayer2;
	String nomePlayer3;
	private int pontuacao = 0;
	private int pontuacao1 = 0;
	private int pontuacao2 = 0;
	int gameOver = 3;

	// Pista
	int PISTA = 10;
	Pista[] pista = new Pista[PISTA];

	// Grama
	Grama grama1 = new Grama();
	Grama grama2 = new Grama();

	// Carro
	Carro carro = new Carro();

	// Cone
	int CONE = 4;
	Cone[] cone = new Cone[CONE];

	// Monstros
	int MONSTRO = 4;
	Cone[] monstro = new Cone[MONSTRO];

	// Arbustos
	int ARBUSTO = 10;
	Arbustos[] arbusto = new Arbustos[ARBUSTO];
	Arbustos[] arbusto2 = new Arbustos[ARBUSTO];

	// Balas
	int BALAS = 4;
	Bala[] bala = new Bala[BALAS];
	int currentBullet = 0;

	// Calcada
	Calcada calcada1 = new Calcada();
	Calcada calcada2 = new Calcada();
	int DIVISORIA = 9;
	Divisoria[] divisoria = new Divisoria[DIVISORIA];
	Divisoria[] divisoria2 = new Divisoria[DIVISORIA];

	// Postes
	int ARVORE = 3;
	Arvore[] arvore1 = new Arvore[ARVORE];
	Arvore[] arvore2 = new Arvore[ARVORE];

	// create the identity transform (0,0)
	AffineTransform identity = new AffineTransform();

	// create a random number generator
	Random rand = new Random();

	Sequencer player;
	String musica1 = "C:/Documents and Settings/LMC3/Desktop/JogoCarro/musica.mid";

	Image VIDA = loadImage("vida.png");
	Image[] vidas = { VIDA, VIDA, VIDA };

	// Escudo
	Escudo escudo = new Escudo();
	Campo campo = new Campo();
	int tempoCampo = 0;
	int tempoEscudo = 30;

	Label Avisos;
	String Pacote;

	public void init() {

		backbuffer = new BufferedImage(480, 640, BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		setSize(480, 480);
		carro.setY(300);
		carro.setX(140);
		carro.setAlive(false);
		grama1.setX(0);
		grama1.setY(0);
		grama2.setX(410);
		grama2.setY(0);
		calcada1.setX(70);
		calcada1.setY(0);
		calcada2.setX(410);
		calcada2.setY(0);

		for (int i = 0; i < PISTA; i++) {
			pista[i] = new Pista();
			pista[i].setX(240);
			pista[i].setY(i * 65);
			pista[i].setVelY(5);
		}

		for (int i = 0; i < CONE; i++) {
			cone[i] = new Cone();
			if (cone[i].isAlive()) {
				cone[i].setX(rand.nextInt(290) + 70);
				cone[i].setY((i * 80) - 1000);
				cone[i].setVelY(5);
			}
			if (cone[i].isAlive() == false) {
				cone[i].setX(rand.nextInt(290) + 70);
				cone[i].setY((i * 80) - 1000);
				cone[i].setVelY(5);
			}
		}

		for (int i = 0; i < MONSTRO; i++) {
			monstro[i] = new Cone();
			if (monstro[i].isAlive()) {
				monstro[i].setX(rand.nextInt(290) + 70);
				monstro[i].setY((i * 100) - 1000);
				monstro[i].setVelY(5);
			}
			if (monstro[i].isAlive() == false) {
				monstro[i].setX(rand.nextInt(290) + 70);
				monstro[i].setY((i * 100) - 1000);
				monstro[i].setVelY(5);
			}
		}

		for (int i = 0; i < ARBUSTO; i++) {
			arbusto[i] = new Arbustos();
			arbusto[i].setX(rand.nextInt(35));
			arbusto[i].setY(i * 100);
			arbusto[i].setVelY(5);

		}

		for (int i = 0; i < ARBUSTO; i++) {
			arbusto2[i] = new Arbustos();
			arbusto2[i].setX(rand.nextInt(40) + 415);
			arbusto2[i].setY(i * 100);
			arbusto2[i].setVelY(5);

		}

		for (int i = 0; i < ARVORE; i++) {
			arvore1[i] = new Arvore();
			arvore1[i].setX(10 - rand.nextInt(9));
			arvore1[i].setY(i * 200);
			arvore1[i].setVelY(5);

		}

		for (int i = 0; i < ARVORE; i++) {
			arvore2[i] = new Arvore();
			arvore2[i].setX(400);
			arvore2[i].setY(i * 200);
			arvore2[i].setVelY(5);

		}

		for (int i = 0; i < DIVISORIA; i++) {
			divisoria[i] = new Divisoria();
			divisoria[i].setX((int) pista[i].getX() - 200);
			divisoria[i].setY(i * 65);
			divisoria[i].setVelY(5);

		}

		for (int i = 0; i < DIVISORIA; i++) {
			divisoria2[i] = new Divisoria();
			divisoria2[i].setX((int) pista[i].getX() + 140);
			divisoria2[i].setY(i * 65);
			divisoria2[i].setVelY(5);

		}

		// set up the bullets
		for (int n = 0; n < BALAS; n++) {
			bala[n] = new Bala();
		}

		// Escudo
		escudo.setX(rand.nextInt(290) + 70);
		escudo.setY(-3000);
		escudo.setVelY(5);
		escudo.setAlive(true);

		// tocarMusica(musica1, gameOver);
		addKeyListener(this);
		addMouseListener(this);
	}

	public void update(Graphics g) {

		Image img = loadImage("carro.png");
		// Image img2 = loadImage("placar.png");
		Image img3 = loadImage("ifce.png");
		// Image img4 = loadImage("carro2.png");
		g2d.setTransform(identity);
		g2d.setPaint(Color.GRAY);
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		if (carro.isAlive() == false) {
			drawMenu();
		}
		if (carro.isAlive()) {
			drawPista();
			drawGrama1();
			drawGrama2();
			// drawCalcada1();
			// drawCalcada2();
			drawDivisoria();
			drawDivisoria2();
			drawArvore();
			drawArvore2();
			drawBala();
			drawCone();
			drawMonstro();
			drawArbusto();
			drawArbusto2();
			if (escudo.isAlive()) {
				drawEscudo();
			}
			if (campo.isAlive()) {
				drawCampo();
			}
			// drawCarro();
			g2d.setTransform(identity);
			g2d.drawImage(img, (int) (carro.getX()), (int) (carro.getY()), 44,
					87, this);
			g2d.drawImage(img3, (int) (carro.getX()) + 13,
					(int) (carro.getY()) + 38, 20, 23, this);
			// g2d.drawImage(img2, 275, 7, 201, 63, this);
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Comic Sans MS", 1, 20));
			g2d.drawString("Pontuação: ", 270, 28);
			g2d.drawString("" + pontuacao, 382, 29);
			g2d.drawImage(vidas[0], 280, 35, 29, 29, this);
			g2d.drawImage(vidas[1], 315, 35, 29, 29, this);
			g2d.drawImage(vidas[2], 350, 35, 29, 29, this);
			g2d.setFont(new Font("Comic Sans MS", 1, 15));
			g2d.drawString("Tempo de escudo: ", 265, 83);
			g2d.drawString("" + tempoEscudo, 396, 83);
			if (gameOver == 0) {
				g2d.setTransform(identity);
				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("Comic Sans MS", 1, 50));
				g2d.drawString("GAME OVER", 100, 200);
				g2d.setFont(new Font("Comic Sans MS", 1, 25));
				g2d.drawString("SUA PONTUAÇÃO: " + pontuacao, 118, 250);
				g2d.setFont(new Font("Comic Sans MS", 1, 20));
				g2d.drawString("Clique AQUI para voltar ao menu", 95, 450);
			}
		}
		paint(g);
	}

	private Image loadImage(String fileName) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image im = toolkit.getImage(fileName);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(im, 0);
		try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException ie) {
		}

		return im;
	}

	public void tocarMusica(String nome, int repetir) {
		try {
			player = MidiSystem.getSequencer();
			Sequence musica = MidiSystem.getSequence(new File(nome));
			player.open();
			player.setSequence(musica);
			player.setLoopCount(repetir);
			player.start();
		} catch (Exception e) {
			System.out.println("Erro ao tocar: " + nome);
		}
	}

	public void drawMenu() {
		Image img = loadImage("play.png");
		Image img2 = loadImage("race.png");
		Image img3 = loadImage("logo.png");
		Image img4 = loadImage("bandeira.png");
		Image carro = loadImage("carro.png");
		Image carro2 = loadImage("carro2.png");
		Image carro3 = loadImage("carro3.png");
		Image carro4 = loadImage("carro4.png");
		Image trofeu = loadImage("trophy.png");
		Image fundo = loadImage("fundo.jpg");
		Image bt = loadImage("bt.png");
		g2d.setTransform(identity);
		g2d.setColor(Color.DARK_GRAY);
		g2d.fill(getBounds());
		g2d.setTransform(identity);
		g2d.drawImage(fundo, 0, 0, 640, 480, this);
		g2d.drawImage(img, 180, 155, 125, 127, this);
		g2d.drawImage(img2, 10, 10, 193, 128, this);
		g2d.drawImage(img3, 135, 10, 374, 139, this);
		g2d.drawImage(trofeu, 200, 390, 70, 70, this);
		g2d.setColor(Color.BLACK);
		g2d.drawImage(bt, 235, 342, 80, 28, this);
		g2d.drawImage(bt, 330, 312, 80, 28, this);
		g2d.setFont(new Font("Comic Sans MS", 1, 15));
		g2d.drawString("Como Jogar: Clique  AQUI!", 190, 330);
		g2d.drawString("Clique   AQUI   para sair!", 190, 360);
		g2d.drawString("Atual RECORDISTA", 270, 420);
		g2d.drawString("Clique no troféu!", 280, 440);
		g2d.drawImage(img4, 70, 300, 107, 71, this);
		g2d.drawImage(carro, 20, 180, 44, 87, this);
		g2d.drawImage(carro2, 95, 180, 44, 87, this);
		g2d.drawImage(carro3, 350, 180, 44, 88, this);
		g2d.drawImage(carro4, 425, 180, 44, 87, this);

	}

	public void drawGrama1() {
		g2d.setTransform(identity);
		g2d.setColor(Color.green);
		g2d.translate(grama1.getX(), grama1.getY());
		g2d.fill(grama1.getShape());
	}

	public void drawGrama2() {
		g2d.setTransform(identity);
		g2d.setColor(Color.green);
		g2d.translate(grama2.getX(), grama2.getY());
		g2d.fill(grama2.getShape());
	}

	public void drawCalcada1() {
		g2d.setTransform(identity);
		g2d.setColor(Color.black);
		g2d.translate(calcada1.getX(), calcada1.getY());
		g2d.fill(calcada1.getShape());
	}

	public void drawCalcada2() {
		g2d.setTransform(identity);
		g2d.setColor(Color.black);
		g2d.translate(calcada2.getX(), calcada2.getY());
		g2d.fill(calcada2.getShape());
	}

	public void drawCarro() {
		g2d.setTransform(identity);
		g2d.setColor(Color.blue);
		g2d.translate(carro.getX(), carro.getY());
		g2d.fill(carro.getShape());
	}

	public void drawPista() {
		for (int i = 0; i < PISTA; i++) {
			g2d.setTransform(identity);
			g2d.translate(pista[i].getX(), pista[i].getY());
			g2d.setColor(Color.YELLOW);
			g2d.fill(pista[i].getShape());
		}
	}

	public void drawCone() {
		Image img2 = loadImage("cone.png");
		for (int i = 0; i < CONE; i++) {
			if (cone[i].isAlive()) {
				g2d.setTransform(identity);
				g2d.translate(cone[i].getX(), cone[i].getY());
				// g2d.setColor(Color.ORANGE);
				// g2d.fill(cone[i].getShape());
				g2d.setTransform(identity);
				g2d.drawImage(img2, (int) (cone[i].getX()),
						(int) (cone[i].getY()), 50, 50, this);
			}
		}
	}

	public void drawMonstro() {
		Image img3 = loadImage("monstro.png");
		for (int i = 0; i < MONSTRO; i++) {
			if (monstro[i].isAlive()) {
				g2d.setTransform(identity);
				g2d.translate(monstro[i].getX(), monstro[i].getY());
				// g2d.setColor(Color.ORANGE);
				// g2d.fill(cone[i].getShape());
				g2d.setTransform(identity);
				g2d.drawImage(img3, (int) (monstro[i].getX() - 6),
						(int) (monstro[i].getY()), 60, 57, this);
			}
		}
	}

	public void drawArbusto() {
		Image img4 = loadImage("arbusto.png");
		for (int i = 0; i < ARBUSTO; i++) {

			g2d.setTransform(identity);
			g2d.translate(arbusto[i].getX(), arbusto[i].getY());
			// g2d.setColor(Color.ORANGE);
			// g2d.fill(cone[i].getShape());
			g2d.setTransform(identity);
			g2d.drawImage(img4, (int) (arbusto[i].getX()),
					(int) (arbusto[i].getY()), 46, 33, this);

		}
	}

	public void drawArbusto2() {
		Image img4 = loadImage("arbusto.png");
		for (int i = 0; i < ARBUSTO; i++) {

			g2d.setTransform(identity);
			g2d.translate(arbusto2[i].getX(), arbusto2[i].getY());
			// g2d.setColor(Color.ORANGE);
			// g2d.fill(cone[i].getShape());
			g2d.setTransform(identity);
			g2d.drawImage(img4, (int) (arbusto2[i].getX()),
					(int) (arbusto2[i].getY()), 46, 33, this);

		}
	}

	public void drawArvore() {
		Image img = loadImage("arvore.png");
		for (int i = 0; i < ARVORE; i++) {
			g2d.setTransform(identity);
			g2d.translate(arvore1[i].getX(), arvore1[i].getY());
			g2d.setTransform(identity);
			g2d.drawImage(img, (int) (arvore1[i].getX()),
					(int) (arvore1[i].getY()), 99, 120, this);
		}

	}

	public void drawArvore2() {
		Image img = loadImage("arvore.png");
		for (int i = 0; i < ARVORE; i++) {
			g2d.setTransform(identity);
			g2d.translate(arvore2[i].getX(), arvore2[i].getY());
			g2d.setTransform(identity);
			g2d.drawImage(img, (int) (arvore2[i].getX()),
					(int) (arvore2[i].getY()), 99, 120, this);
		}

	}

	public void drawDivisoria() {
		Image img6 = loadImage("madeira.png");
		for (int i = 0; i < DIVISORIA; i++) {
			if (divisoria[i].isAlive()) {
				g2d.setTransform(identity);
				g2d.translate(divisoria[i].getX(), divisoria[i].getY());
				// g2d.setColor(Color.ORANGE);
				// g2d.fill(cone[i].getShape());
				g2d.setTransform(identity);
				g2d.drawImage(img6, (int) pista[i].getX() - 200,
						(int) pista[i].getY() - 50, 80, 160, this);
			}
		}
	}

	public void drawDivisoria2() {
		Image img6 = loadImage("madeira.png");
		for (int i = 0; i < DIVISORIA; i++) {
			if (divisoria2[i].isAlive()) {
				g2d.setTransform(identity);
				g2d.translate(divisoria2[i].getX(), divisoria2[i].getY());
				// g2d.setColor(Color.ORANGE);
				// g2d.fill(cone[i].getShape());
				g2d.setTransform(identity);
				g2d.drawImage(img6, (int) pista[i].getX() + 140,
						(int) pista[i].getY() - 50, 80, 160, this);
				;
			}
		}
	}

	public void drawEscudo() {
		Image img = loadImage("escudo.png");
		g2d.setTransform(identity);
		g2d.translate(escudo.getX(), escudo.getY());
		g2d.setTransform(identity);
		g2d.drawImage(img, (int) (escudo.getX()), (int) (escudo.getY()), 33,
				42, this);
	}

	public void drawCampo() {
		Image img = loadImage("campo.png");
		g2d.setTransform(identity);
		g2d.translate(campo.getX(), campo.getY());
		g2d.setTransform(identity);
		g2d.setColor(Color.BLUE);
		g2d.drawRect((int) carro.getX() - 3, (int) carro.getY(), 50, 90);
		g2d.drawImage(img, (int) (carro.getX()) - 21,
				(int) (carro.getY() - 22), 83, 138, this);
	}

	public void drawBala() {
		Image img5 = loadImage("bala.png");
		for (int n = 0; n < BALAS; n++) {
			// checar se bala esta viva
			if (bala[n].isAlive()) {
				// Desenha Bala
				g2d.setTransform(identity);
				g2d.translate(bala[n].getX(), bala[n].getY());
				g2d.fill(bala[n].getShape());
				g2d.setColor(Color.MAGENTA);
				g2d.draw(bala[n].getShape());
				g2d.setTransform(identity);
				g2d.drawImage(img5, (int) (bala[n].getX()) - 10,
						(int) (bala[n].getY() - 15), 21, 45, this);
			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(backbuffer, 0, 0, this);

	}

	public void start() {
		gameloop = new Thread(this);
		gameloop.start();
	}

	public void run() {

		Thread t = Thread.currentThread();

		while (t == gameloop) {

			try {

				gameUpdate();
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			repaint();
		}
	}

	public void stop() {
		gameloop = null;
	}

	private void gameUpdate() {

		if (carro.isAlive() && gameOver > 0) {
			updatePista();
			updateCarro();
			updateBala();
			Colisao();
			updateCone();
			updateMonstro();
			updateArbusto();
			updateArbusto2();
			updateArvore();
			updateArvore2();
			updateEscudo();
		}

	}

	public void updatePista() {

		for (int i = 0; i < PISTA; i++) {
			pista[i].incY(pista[i].getVelY());
			if (pista[i].getY() == 530) {
				pista[i].setY(-50);
				if (campo.isAlive() && tempoCampo < 30) {
					tempoCampo++;
					tempoEscudo--;
					if (tempoCampo == 30) {
						campo.setAlive(false);
						tempoCampo = 0;
						tempoEscudo = 30;
					}
				}
			}
		}
	}

	public void updateCone() {

		for (int i = 0; i < CONE; i++) {
			// if(cone[i].isAlive()){
			cone[i].incY(cone[i].getVelY());
			if (cone[i].getY() == 530) {
				cone[i].setY(-50);
				cone[i].setX(rand.nextInt(290) + 80);
				if (cone[i].isAlive()) {
					pontuacao = pontuacao + 5;

				}
				cone[i].setAlive(true);
			}

		}
		// }
	}

	public void updateEscudo() {

		// if(cone[i].isAlive()){
		escudo.incY(escudo.getVelY());
		if (escudo.getY() == 530) {
			escudo.setY(-3000);
			escudo.setX(rand.nextInt(290) + 80);
			escudo.setAlive(true);
		}

		// }
	}

	public void updateMonstro() {

		for (int i = 0; i < MONSTRO; i++) {
			// if(monstro[i].isAlive()){
			monstro[i].incY(monstro[i].getVelY());
			if (monstro[i].getY() == 530) {
				monstro[i].setY(-50);
				monstro[i].setX(rand.nextInt(290) + 80);
				monstro[i].setAlive(true);
			}
			// }
		}
	}

	public void updateArbusto() {

		for (int i = 0; i < ARBUSTO; i++) {

			arbusto[i].incY(arbusto[i].getVelY());
			if (arbusto[i].getY() == 530) {
				arbusto[i].setY(-50);
				arbusto[i].setX(rand.nextInt(35));
			}
		}
	}

	public void updateArbusto2() {

		for (int i = 0; i < ARBUSTO; i++) {

			arbusto2[i].incY(arbusto2[i].getVelY());
			if (arbusto2[i].getY() == 530) {
				arbusto2[i].setY(-50);
				arbusto2[i].setX(rand.nextInt(40) + 415);
			}
		}
	}

	public void updateArvore() {

		for (int i = 0; i < ARVORE; i++) {

			arvore1[i].incY(arvore1[i].getVelY());
			if (arvore1[i].getY() == 530) {
				arvore1[i].setY(-115);
				arvore1[i].setX(10 - rand.nextInt(9));
			}
		}
	}

	public void updateArvore2() {

		for (int i = 0; i < ARVORE; i++) {

			arvore2[i].incY(arvore2[i].getVelY());
			if (arvore2[i].getY() == 530) {
				arvore2[i].setY(-115);
				arvore2[i].setX(400 + rand.nextInt(9));
			}
		}
	}

	public void updateBala() {
		// movimento de cada bala
		for (int n = 0; n < BALAS; n++) {
			// checar se bala esta sendo usada
			if (bala[n].isAlive()) {
				// atualiza posiÃ§Ã£o da bala
				bala[n].incY(bala[n].getVelY());
				// Bala desaparece ao atingir o topo da tela
				if (bala[n].getY() < 0) {
					bala[n].setAlive(false);
				}
			}
		}
	}

	public void updateDivisoria() {

		for (int i = 0; i < DIVISORIA; i++) {

			divisoria[i].incY(divisoria[i].getVelY());

			if (divisoria[i].getY() == 530) {
				divisoria[i].setY(-200);
				divisoria[i].setX((int) pista[i].getX() - 200);
			}
		}

	}

	public void updateDivisoria2() {

		for (int i = 0; i < DIVISORIA; i++) {

			divisoria2[i].incY(divisoria2[i].getVelY());
			if (divisoria2[i].getY() == 530) {
				divisoria2[i].setY(-200);
				divisoria2[i].setX((int) pista[i].getX() + 140);
			}
		}

	}

	public void updateCarro() {
	}

	public void Colisao() {
		if (gameOver > 0) {
			if (campo.isAlive() == false) {
				for (int m = 0; m < CONE; m++) {
					if (cone[m].isAlive() == true) {
						if (cone[m].getBounds().intersects(carro.getBounds())) {
							cone[m].setAlive(false);
							gameOver--;
							vidas[gameOver] = null;
							if (gameOver == 0) {
								vidas[0] = null;
								g2d.setColor(Color.BLACK);
								g2d.setFont(new Font("Arial", 1, 20));
								g2d.drawString("GAME OVER", 200, 200);
							}

							continue;
						}
					}

				}

				// Colisão Mosntro com Carro
				for (int m = 0; m < MONSTRO; m++) {

					if (monstro[m].isAlive() == true) {
						if (monstro[m].getBounds()
								.intersects(carro.getBounds())) {
							monstro[m].setAlive(false);
							gameOver--;
							vidas[gameOver] = null;
							if (gameOver == 0) {
								vidas[0] = null;
							}

							continue;
						}
					}
				}
			}
			// Colisão Bala Com Mosntros e Cones
			for (int i = 0; i < BALAS; i++) {
				if (bala[i].isAlive()) {
					// Colisão com Mosntros
					for (int n = 0; n < MONSTRO; n++) {
						if (monstro[n].isAlive()) {
							if (bala[i].getBounds().intersects(
									monstro[n].getBounds())) {
								bala[i].setAlive(false);
								monstro[n].setAlive(false);
								pontuacao = pontuacao + 10;

								continue;
							}
						}
					}
					// Colisão com Cone
					for (int n = 0; n < CONE; n++) {
						if (cone[n].isAlive()) {
							if (bala[i].getBounds().intersects(
									cone[n].getBounds())) {
								bala[i].setAlive(false);

								continue;
							}
						}
					}

				}
			}

			if (escudo.getBounds().intersects(carro.getBounds())) {
				campo.setAlive(true);
				escudo.setAlive(false);
			}
			if (campo.isAlive()) {
				for (int m = 0; m < CONE; m++) {
					if (cone[m].isAlive() == true) {
						if (cone[m].getBounds().intersects(carro.getBounds())) {
							cone[m].setAlive(false);
							pontuacao = pontuacao + 5;
						}

						continue;
					}
				}

				// Colisão Mosntro com Carro
				for (int m = 0; m < MONSTRO; m++) {

					if (monstro[m].isAlive() == true) {
						if (monstro[m].getBounds()
								.intersects(carro.getBounds())) {
							monstro[m].setAlive(false);
							pontuacao = pontuacao + 5;
							continue;
						}
					}
				}
			}
		}

	}

	/*****************************************************
	 * key listener events
	 *****************************************************/
	public void keyReleased(KeyEvent k) {
	}

	public void keyTyped(KeyEvent k) {
	}

	public void keyPressed(KeyEvent k) {
		int keyCode = k.getKeyCode();
		if (gameOver > 0) {
			switch (keyCode) {

			case KeyEvent.VK_LEFT:
				if (carro.getX() >= 83) {
					carro.setX(carro.getX() - 8);
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (carro.getX() <= 367) {
					carro.setX(carro.getX() + 8);
				}
				break;
			case KeyEvent.VK_DOWN:
				carro.setY(carro.getY() + 8);
				break;
			case KeyEvent.VK_UP:
				carro.setY(carro.getY() - 8);
				break;
			case KeyEvent.VK_ENTER:
				// fire a bullet
				currentBullet++;
				if (currentBullet > BALAS - 1)
					currentBullet = 0;
				bala[currentBullet].setAlive(true);

				// point bullet in same direction ship is facing
				bala[currentBullet].setX(carro.getX() + 20);
				bala[currentBullet].setY(carro.getY());
				bala[currentBullet].setMoveAngle(90);
				bala[currentBullet].setVelY(-5);
				break;

			}
		}
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent e) {
		if (gameOver > 0) {
			if (e.getX() > 179 && e.getX() < 304 && e.getY() > 156
					&& e.getY() < 279) {
				carro.setAlive(true);
				gameOver = 3;
				tocarMusica(musica1, gameOver);

			}
		}
		if (e.getX() > 237 && e.getX() < 309 && e.getY() > 343
				&& e.getY() < 364 && carro.isAlive()== false) {

			JOptionPane.showMessageDialog(null, "Volte Sempre!");
			System.exit(0);
		}
		if (e.getX() > 332 && e.getX() < 404 && e.getY() > 313
				&& e.getY() < 335 && carro.isAlive()== false) {
			System.out.println(e.getY());
			JOptionPane
					.showMessageDialog(
							null,
							"<html>Use as setas do teclado Para mover o carro!"
									+ "<br>Use a tecla ENTER para atirar(Use para matar os MONSTROS!)"
									+ "<br>Desvie dos CONES! <br>Cada CONE ou MONSTRO desviado: +5 pontos!"
									+ "<br>Matar Monstro com bala: +10 PONTOS!"
									+ "<br> A cada colisão você perde 1 VIDA!"
									+ "<br> Pegue os escudos, e fique IMUNE a cones e monstros!"
									+ "<br>Bom jogo, Divirta-se!</html>");
		}

		if (e.getX() > 211 && e.getX() < 254 && e.getY() > 390
				&& e.getY() < 460 && carro.isAlive()== false) {

			try {
				mostrarPontos();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
		}

		if (gameOver == 0 && carro.isAlive()) {
			if (e.getX() > 160 && e.getX() < 208 && e.getY() > 431
					&& e.getY() < 453) {
				try {
					if (pontuacao > recuperarPontos()) {
						try {
							nomePlayer = JOptionPane
									.showInputDialog("<html>Você é o novo RECORDISTA! <br>digite seu nome para"
											+ " cravá-lo no nosso menu inical!</html>");
							armazenarPontos();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
						}
					}
					if (pontuacao > recuperarPontos2()
							&& pontuacao < recuperarPontos()) {
						try {
							nomePlayer = JOptionPane
									.showInputDialog("<html>Você é o segundo! <br>digite seu nome para"
											+ " cravá-lo no nosso menu inical!</html>");
							armazenarPontos2();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
						}
					}
					if (pontuacao > recuperarPontos3()
							&& pontuacao < recuperarPontos2()) {
						try {
							nomePlayer = JOptionPane
									.showInputDialog("<html>Você é o terceiro! <br>digite seu nome para"
											+ " cravá-lo no nosso menu inical!</html>");

							armazenarPontos3();

						} catch (IOException e1) {
							// TODO Auto-generated catch block
						}
					}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
				}
				carro.setAlive(false);
				vidas[0] = VIDA;
				vidas[1] = VIDA;
				vidas[2] = VIDA;
				gameOver = 3;
				pontuacao = 0;
				player.stop();
				for (int i = 0; i < MONSTRO; i++) {
					monstro[i].setY((i * 100) - 1000);
					monstro[i].setX(rand.nextInt(290) + 80);

				}
				for (int i = 0; i < CONE; i++) {
					cone[i].setY((i * 80) - 1000);
					cone[i].setX(rand.nextInt(290) + 80);

				}
				stop();
				start();

			}
		}
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public void armazenarPontos() throws IOException {
		recuperarNomes();
		try (BufferedWriter writer = Files.newBufferedWriter(path, utf8)) {
			writer.write(nomePlayer + " - " + pontuacao + " - " + nomePlayer1
					+ " - " + pontuacao1 + " - " + nomePlayer2 + " - "
					+ pontuacao2);

		}

	}

	public void armazenarPontos2() throws IOException {
		recuperarNomes();
		try (BufferedWriter writer = Files.newBufferedWriter(path, utf8)) {
			writer.write(nomePlayer1 + " - " + pontuacao1 + " - " + nomePlayer
					+ " - " + pontuacao + " - " + nomePlayer2 + " - "
					+ pontuacao2);

		}

	}

	public void armazenarPontos3() throws IOException {
		recuperarNomes();
		try (BufferedWriter writer = Files.newBufferedWriter(path, utf8)) {
			writer.write(nomePlayer1 + " - " + pontuacao1 + " - " + nomePlayer2
					+ " - " + pontuacao2 + " - " + nomePlayer + " - "
					+ pontuacao);

		}

	}

	public int recuperarPontos() throws IOException {
		int pj = 0;
		try (BufferedReader reader = Files.newBufferedReader(path, utf8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] t = line.split(" - ");
				pj = Integer.parseInt(t[1]);
			}
		}

		return pj;
	}

	public int recuperarPontos2() throws IOException {
		int pj = 0;
		try (BufferedReader reader = Files.newBufferedReader(path, utf8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] t = line.split(" - ");
				pj = Integer.parseInt(t[3]);
			}
		}

		return pj;
	}

	public int recuperarPontos3() throws IOException {
		int pj = 0;
		try (BufferedReader reader = Files.newBufferedReader(path, utf8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] t = line.split(" - ");

				pj = Integer.parseInt(t[5]);
			}
		}

		return pj;
	}

	public void mostrarPontos() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, utf8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] t = line.split(" - ");
				JOptionPane.showMessageDialog(null, "Ranking:\n" + "Primeiro: "
						+ t[0] + ", Pontuação: " + t[1] + "\nSegundo: " + t[2]
						+ ", Pontuação: " + t[3] + "\nTerceiro: " + t[4]
						+ ", Pontuação: " + t[5]);
			}
		}

	}

	public void recuperarNomes() throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, utf8)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] t = line.split(" - ");
				nomePlayer1 = t[0];
				pontuacao1 = Integer.parseInt(t[1]);
				nomePlayer2 = t[2];
				pontuacao2 = Integer.parseInt(t[3]);
				nomePlayer3 = t[4];
				
			}
		}

	}

}
