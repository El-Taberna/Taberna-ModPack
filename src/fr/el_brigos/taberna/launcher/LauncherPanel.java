package fr.el_brigos.taberna.launcher;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;
import static fr.theshark34.swinger.Swinger.getResource;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener {
	
	private Image background = getResource("background.png");
	
	private Saver saver = new Saver(new File(Launcher.TA_DIR, "launcher.properties"));
	
	private JTextField usernameField = new JTextField(this.saver.get("username"));
	private JPasswordField passwordField = new JPasswordField();
	
	private STexturedButton playButton = new STexturedButton(getResource("play.png"),Swinger.getResource("play.png"),Swinger.getResource("play.png"));
	private STexturedButton quitButton = new STexturedButton(getResource("quit.png"));
	private STexturedButton hideButton = new STexturedButton(getResource("hide.png"));
	private STexturedButton ramButton = new STexturedButton(getResource("ram.png"));
	
	private SColoredBar progressBar = new SColoredBar(Swinger.getTransparentWhite(25), Swinger.getTransparentWhite(75));
	private JLabel infoLabel = new JLabel("Tu veux jouer ? Clique sur le canard !", SwingConstants.CENTER);
	
	private RamSelector ramSelector = new RamSelector(new File(Launcher.TA_DIR, "Ram.txt"));
	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setFont(usernameField.getFont().deriveFont(20F));
		usernameField.setBounds(614, 482, 338, 33);
		this.add(usernameField);

		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setFont(usernameField.getFont());
		passwordField.setBounds(614, 534, 338, 33);
		this.add(passwordField);
		
		playButton.setBounds(275, 270);
		playButton.addEventListener(this);
		this.add(playButton);
		
		quitButton.setBounds(925, 0);
		quitButton.addEventListener(this);
		this.add(quitButton);
		
		hideButton.setBounds(875, 0);
		hideButton.addEventListener(this);
		this.add(hideButton);
		
		ramButton.setBounds(825, 0);
		ramButton.addEventListener(this);
		this.add(ramButton);
		
		progressBar.setBounds(34, 534, 534, 33);
		progressBar.setVisible(true);
		add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		infoLabel.setBounds(34, 534, 534, 33);
		add(infoLabel);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEvent (SwingerEvent e) {
		if(e.getSource() == playButton) {
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0) {
				JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un email et un mot de passe valides.",  "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, Impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					saver.set("username", usernameField.getText());
					ramSelector.save();
					
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptUpdateThread();
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, Impossible de mettre le jeu à jour : " + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					try {
						Launcher.launch();
					} catch (LaunchException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, Impossible de lancer le jeu : " + e, "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
					}
				}
			};
			t.start();
		} else if(e.getSource() == quitButton)
			System.exit(0);
		else if(e.getSource() == hideButton)
			LauncherFrame.getInstance().setState(JFrame.ICONIFIED);
		else if(e.getSource() == ramButton)
			ramSelector.display();
	}
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		drawFullsizedImage(graphics, this, background);
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public SColoredBar getProgressBar()
	{
		return progressBar;
	}

	public void setInfoText(String infoText)
	{
		infoLabel.setText(infoText);
	}
	
	public RamSelector getRamSelector() 
	{
		return ramSelector;
	}

}
