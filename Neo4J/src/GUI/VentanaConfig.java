package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.JdbcNeo4jConnector;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaConfig extends JDialog {
	
	
	private JdbcNeo4jConnector neo4j;
	private JPanel pnFormulario;
	private JPanel pnBotones;
	private JLabel lblUrl;
	private JLabel lblUsuario;
	private JLabel lblContrasea;
	private JTextField txtUrl;
	private JTextField txtUsuario;
	private JPasswordField psContrasena;
	private JButton btnAceptar;
	private JButton btnCancelar;

	public VentanaConfig(JdbcNeo4jConnector neo4j) {
		this.neo4j = neo4j;
		setModal(true);
		setResizable(false);
		setSize(340,180);
		setLocationRelativeTo(null);
		getContentPane().add(getPnFormulario(), BorderLayout.CENTER);
		getContentPane().add(getPnBotones(), BorderLayout.SOUTH);
		
		setVisible(true);
		

	}

	private JPanel getPnFormulario() {
		if (pnFormulario == null) {
			pnFormulario = new JPanel();
			pnFormulario.setLayout(new MigLayout("", "[][grow]", "[][][]"));
			pnFormulario.add(getLblUrl(), "cell 0 0,alignx trailing");
			pnFormulario.add(getTxtUrl(), "cell 1 0,growx");
			pnFormulario.add(getLblUsuario(), "cell 0 1,alignx trailing");
			pnFormulario.add(getTxtUsuario(), "cell 1 1,growx");
			pnFormulario.add(getLblContrasea(), "cell 0 2,alignx trailing");
			pnFormulario.add(getPsContrasena(), "cell 1 2,growx");
		}
		return pnFormulario;
	}
	private JPanel getPnBotones() {
		if (pnBotones == null) {
			pnBotones = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			pnBotones.add(getBtnAceptar());
			pnBotones.add(getBtnCancelar());
		}
		return pnBotones;
	}
	private JLabel getLblUrl() {
		if (lblUrl == null) {
			lblUrl = new JLabel("URL:");
		}
		return lblUrl;
	}
	private JLabel getLblUsuario() {
		if (lblUsuario == null) {
			lblUsuario = new JLabel("Usuario:");
		}
		return lblUsuario;
	}
	private JLabel getLblContrasea() {
		if (lblContrasea == null) {
			lblContrasea = new JLabel("Contrase\u00F1a:");
		}
		return lblContrasea;
	}
	private JTextField getTxtUrl() {
		if (txtUrl == null) {
			txtUrl = new JTextField();
			txtUrl.setText("bolt://127.0.0.1:7687");
			txtUrl.setColumns(10);
		}
		return txtUrl;
	}
	private JTextField getTxtUsuario() {
		if (txtUsuario == null) {
			txtUsuario = new JTextField();
			txtUsuario.setText("neo4j");
			txtUsuario.setColumns(10);
		}
		return txtUsuario;
	}
	private JPasswordField getPsContrasena() {
		if (psContrasena == null) {
			psContrasena = new JPasswordField();
			psContrasena.setText("g1");
		}
		return psContrasena;
	}
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					neo4j.setURL(txtUrl.getText());
					neo4j.setUser(txtUsuario.getText());
					neo4j.setPassword(psContrasena.getText());
					dispose();
				}
			});
		}
		return btnAceptar;
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return btnCancelar;
	}
}
