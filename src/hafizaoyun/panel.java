package hafizaoyun;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
class Kart {
    private static Image[] resimler;
	private static String[] resimadlari = {
           //  "a.png",
			"elma.png", 
			"bank.png", 
			"basketbol.png", 
			"mavi.png",
			"yesil.png",
			"kirmizi.png",
			"insa.png",
			"kedi.png",
			"peynir.png",
			"denture.png",
			"dog.png",
			"hokey.png",
			"anahtar.png","telefon.png",
			"pizza.png","ns.png",
			"futbol.png",
			"sock.png",
                        "cam.png",
               
        };
        private static Image arkaresim;
	public static final int boyut = 100;
	public static final int pd = 5;
	public static final int a = 10;
        private Image resim; 
	private int x, y; 
        private boolean sr;
	private boolean md;
	static{
		arkaresim= new ImageIcon("arkaplan.png").getImage();
		resimler = new Image[resimadlari.length];
		for(int i=0; i<resimadlari.length; i++){
			resimler[i] = new ImageIcon( resimadlari[i]).getImage();
		}
	}
	public Kart(int imageIndex, int x, int y){
		resim = resimler[imageIndex];
		this.x = x;
		this.y = y;
	}
	
	public boolean esle(Kart kart){
		return resim == kart.resim;
	}
	public void resimdegisme(Kart kart){
		Image x = resim;
		resim = kart.resim;
		kart.resim = x;}
	public void göster(){sr=true;}
	public void gizle(){sr=false;}
	public void setmd(){md=true;}
	public boolean eslendi(){return md;}
	public boolean secilen(int mx, int my){
		return mx>x && mx<x+boyut && my>y && my<y+boyut;
        }public void draw(Graphics g){
		if (md) return; 
		if (sr) g.drawImage(resim, x+a, y+a, boyut-2*a, boyut-2*a, null);
		else g.drawImage(arkaresim, x+a, y+a, boyut-2*a, boyut-2*a, null);
		g.setColor(Color.black);
		g.drawRect(x, y, boyut, boyut);
		g.setColor(Color.black);
		g.drawRect(x+pd, y+pd, boyut-2*pd, boyut-2*pd);
	}}
public class panel extends JPanel implements MouseListener{
    public static final int kartsayisi = 36;
	public static final int sütun = 6;
	public static final int kenar = 10;
	private JFrame frame;
	private Kart[] kartlar;
	private int puan = 0;
	private long sonkart;
	private boolean bossec = true, bir, iki;
	private Kart ilksec, ikincisec;
        public panel(){
		int width = 2*kenar + sütun*Kart.boyut;
		int height = 2*kenar + (kartsayisi/sütun)*Kart.boyut;
		if (kartsayisi%sütun != 0) height += Kart.boyut;
		frame = new JFrame("Puanınız: " + puan);
		frame.setBounds(400, 50, 0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(width,height));
		frame.getContentPane().add(this);
		kartlar= new Kart[kartsayisi];
		for(int i=0; i<kartlar.length; i++){
			kartlar[i] = new Kart(i/2, kenar + i%sütun*Kart.boyut, kenar + i/sütun*Kart.boyut);
		}
		for(int i=0; i<kartlar.length; i++){
			kartlar[i].resimdegisme(kartlar[(int)(Math.random()*kartlar.length)]);}
		frame.pack();
		frame.setVisible(true);
		this.addMouseListener(this);
	}
	public void paintComponent(Graphics g){
	
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		for(int i=0; i<kartlar.length; i++){
			kartlar[i].draw(g);
		}}
	public void mousePressed(MouseEvent e){
		for(int i=0; i<kartlar.length; i++){
			if(!kartlar[i].eslendi() && kartlar[i].secilen(e.getX(), e.getY())){
				if(bossec){
					ilksec = kartlar[i];
					ilksec .göster();
					bossec = false;
					bir = true;		
				}
				else if (bir){
					if(kartlar[i]==ilksec ) return;
					ikincisec = kartlar[i];
					ikincisec.göster();
					bir = false;
					iki = true;
					sonkart = System.currentTimeMillis();
                                }this.repaint();break;}}}
        
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void applyGameLogic(){
		if(!iki || System.currentTimeMillis()-sonkart < 2000) return;
		if (ilksec .esle(ikincisec)){
			ilksec .setmd();
			ikincisec.setmd();
			puan += 10;
			frame.setTitle("Puanınız: " + puan);	
		}
		else{
			ilksec .gizle();
			ikincisec.gizle();
			puan -= 1;
			frame.setTitle("Puanınız: " + puan);
		}
		this.repaint();
		iki = false;
		bossec = true;
		ilksec  = null;
		ikincisec = null;
	}
	public void göster(){for(int i=0; i<kartlar.length; i++) kartlar[i].göster();}
	public void gizle(){for(int i=0; i<kartlar.length; i++) kartlar[i].gizle();}
	public static void main(String[] args) {
		panel panel1 = new panel();
		panel1.göster();
		panel1.repaint();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		panel1.gizle();
		panel1.repaint();
		while(true){
			panel1.applyGameLogic();
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {}}}}
