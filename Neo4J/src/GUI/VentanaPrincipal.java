package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import logic.JdbcNeo4jConnector;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JButton btnApplyManualQuery;
	private JPanelQueryResult panelQueryResult;
	private JPanel pnManualQuery;
	private JTextArea txManualQuery;
	private JScrollPane scrollPane;
	private JPanel pnPremadeQuery;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NimbusLookAndFeel laf = new NimbusLookAndFeel();
					UIManager.setLookAndFeel(laf);
					
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[107.00][105.00][grow]"));
		contentPane.add(getPnPremadeQuery(), "cell 0 0,grow");
		contentPane.add(getPnManualQuery(), "cell 0 1,grow");
		contentPane.add(getPanelQueryResult(), "cell 0 2,grow");
		
	}
	private JButton getBtnApplyManualQuery() {
		if (btnApplyManualQuery == null) {
			btnApplyManualQuery = new JButton("OK");
			btnApplyManualQuery.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					String result = JdbcNeo4jConnector.premadeQuery( txManualQuery.getText() );
					getPanelQueryResult().addOutput( result );
				}
			});
		}
		return btnApplyManualQuery;
	}
	private JPanelQueryResult getPanelQueryResult() {
		if (panelQueryResult == null) {
			panelQueryResult = new JPanelQueryResult();
		}
		return panelQueryResult;
	}
	private JPanel getPnManualQuery() {
		if (pnManualQuery == null) {
			pnManualQuery = new JPanel();
			pnManualQuery.setBorder(new TitledBorder(null, "Consulta libre", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnManualQuery.setForeground(Color.LIGHT_GRAY);
			pnManualQuery.setBackground(Color.WHITE);
			pnManualQuery.setLayout(new MigLayout("", "[47px][grow]", "[53.00px,grow,fill]"));
			pnManualQuery.add(getBtnApplyManualQuery(), "cell 0 0,growx,aligny top");
			pnManualQuery.add(getScrollPane(), "cell 1 0,grow");
		}
		return pnManualQuery;
	}
	private JTextArea getTxManualQuery() {
		if (txManualQuery == null) {
			txManualQuery = new JTextArea();
		}
		return txManualQuery;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTxManualQuery());
		}
		return scrollPane;
	}
	private JPanel getPnPremadeQuery() {
		if (pnPremadeQuery == null) {
			pnPremadeQuery = new JPanel();
			pnPremadeQuery.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Consultas pre-programadas", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			pnPremadeQuery.setBackground(Color.WHITE);
		}
		return pnPremadeQuery;
	}
}
