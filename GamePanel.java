import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int TELA_LARGURA = 600;
    static final int TELA_ALTURA = 600;
    static final int TAMANHO_UNIDADES = 25;
    static final int JOGO_UNIDADES = (TELA_LARGURA*TELA_ALTURA)/TAMANHO_UNIDADES;
    static final int ATRASO = 115;
    final int x[] = new int[JOGO_UNIDADES];
    final int y[] = new int[JOGO_UNIDADES];
    int partesCorpos = 2, 
        frutasIngeridas, 
        frutaX, 
        frutaY;
    char direcao = 'D';
    boolean rodando = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(TELA_LARGURA,TELA_ALTURA));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        comecarJogo();
    }

    public void comecarJogo() {
        novaFruta();
        rodando = true;
        timer = new Timer(ATRASO,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (rodando) {
            /*
            for(int i = 0; i <TELA_ALTURA/TAMANHO_UNIDADES;i++) {
                g.drawLine(i*TAMANHO_UNIDADES, 0, i*TAMANHO_UNIDADES, TELA_ALTURA);
                g.drawLine(0,i*TAMANHO_UNIDADES, TELA_ALTURA, i*TAMANHO_UNIDADES);
            }
            */
    
            g.setColor(Color.RED);
            g.fillOval(frutaX, frutaY, TAMANHO_UNIDADES, TAMANHO_UNIDADES);
    
            for(int i = 0;i<partesCorpos;i++) {
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillOval(x[i],y[i], TAMANHO_UNIDADES, TAMANHO_UNIDADES);
                }
                else{
                    g.setColor(new Color(45,180,0));
                    g.fillOval(x[i],y[i], TAMANHO_UNIDADES, TAMANHO_UNIDADES);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Pontuação: "+frutasIngeridas, (TELA_LARGURA - metrics.stringWidth("Pontuação: "+frutasIngeridas))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }

    }

    public void novaFruta(){

        frutaX = random.nextInt((int) (TELA_LARGURA/TAMANHO_UNIDADES))*TAMANHO_UNIDADES;
        frutaY = random.nextInt((int) (TELA_ALTURA/TAMANHO_UNIDADES))*TAMANHO_UNIDADES;


    }

    public void mover(){

        for (int i = partesCorpos;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direcao) {
            // C - Cima
            case 'C':
                y[0] = y[0] - TAMANHO_UNIDADES;
                break;
            // B - Baixo
            case 'B':
                y[0] = y[0] + TAMANHO_UNIDADES;
                break;
            // D - Direita
            case 'D':
                x[0] = x[0] + TAMANHO_UNIDADES;
                break;
            // E - Esquerda
            case 'E':
                x[0] = x[0] - TAMANHO_UNIDADES;
                break;
        }
    }

    public void checarFruta(){

        if ((x[0] == frutaX)&&(y[0] == frutaY)){
            partesCorpos++;
            frutasIngeridas++;
            novaFruta();
        }

    }

    public void checarColisao(){

        //Checar para ver se a cabeca encosta no corpo
        for(int i = partesCorpos;i>0;i--){
            if((x[0] == x[i])&&(y[0] == y[i])){
                rodando = false;
            }
        }
        

        //Checar para ver se a cabeça bate na borda de cima
        if(y[0] < 0){
            rodando = false;
        }
        //Checar para ver se a cabeça bate na borda de baixo
        if(y[0] > TELA_ALTURA){
            rodando = false;
        }
        //Checar para ver se a cabeça bate na borda da direita
        if(x[0] > TELA_LARGURA){
            rodando = false;
        }
        
        //Checar para ver se a cabeça bate na borda da esquerda
        if(x[0] < 0){
            rodando = false;
        }

        if (rodando==false){
            timer.stop();
        }
        
    }

    public void gameOver(Graphics g){

        //Texto Fim de JOGO
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Fim de jogo!", (TELA_LARGURA - metrics1.stringWidth("Fim de Jogo!"))/2, TELA_ALTURA/2);

        //Pontuacao
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Pontuação: "+frutasIngeridas, (TELA_LARGURA - metrics2.stringWidth("Pontuação: "+frutasIngeridas))/2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (rodando){
            mover();
            checarFruta();
            checarColisao();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direcao != 'B'){
                        direcao = 'C';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direcao != 'C'){
                        direcao = 'B';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direcao != 'E'){
                        direcao = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direcao != 'D'){
                        direcao = 'E';
                    }
                    break;
            }

        }
    }
}
