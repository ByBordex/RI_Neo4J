package GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.xml.crypto.Data;

import logic.Consulta;
import logic.Consulta.Dificultad;
import logic.JdbcNeo4jConnector;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class VentanaPrincipal extends JFrame {

	private JdbcNeo4jConnector neo4j;
	
	private JPanel contentPane;
	private JButton btnApplyManualQuery;
	private JPanelQueryResult panelQueryResult;
	private JPanel pnManualQuery;
	private JTextArea txManualQuery;
	private JScrollPane scrollPane;
	private JTabbedPane pnPremadeQuery;
	
	private ListenerConsulta listenerConsulta;
	private JScrollPane spSimple;
	private JPanel pnSimple;
	private JScrollPane spIntermedio;
	private JScrollPane spAvanzado;
	private JPanel pnIntermedias;
	private JPanel pnAvanzadas;

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
		listenerConsulta = new ListenerConsulta();
		setResizable(false);
		
		neo4j = new JdbcNeo4jConnector();
		new VentanaConfig( neo4j );
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setForeground(Color.BLUE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[107.00,grow][105.00][grow]"));
		contentPane.add(getPnPremadeQuery(), "cell 0 0,grow");
		contentPane.add(getPnManualQuery(), "cell 0 1,grow");
		contentPane.add(getPanelQueryResult(), "cell 0 2,grow");
		
	}
	private JButton getBtnApplyManualQuery() {
		if (btnApplyManualQuery == null) {
			btnApplyManualQuery = new JButton("OK");
			btnApplyManualQuery.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					String result = neo4j.premadeQuery( txManualQuery.getText() );
					getPanelQueryResult().addOutput(  txManualQuery.getText(), result );
					
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
	private JTabbedPane getPnPremadeQuery() {
		if (pnPremadeQuery == null) {
			pnPremadeQuery = new JTabbedPane(JTabbedPane.TOP);
			pnPremadeQuery.setBackground(Color.WHITE);
			pnPremadeQuery.setBorder(new TitledBorder(null, "Consultas pre-programadas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnPremadeQuery.addTab("B\u00E1sicas", null, getSpSimple(), null);
			pnPremadeQuery.addTab("Intermedias", null, getSpIntermedio(), null);
			pnPremadeQuery.addTab("Avanzadas", null, getSpAvanzado(), null);
		}
		return pnPremadeQuery;
	}
	
	private JScrollPane getSpSimple() {
		if (spSimple == null) {
			spSimple = new JScrollPane();
			spSimple.setViewportView(getPnSimple());
		}
		return spSimple;
	}
	private JPanel getPnSimple() {
		if (pnSimple == null) {
			pnSimple = new JPanel();
			pnSimple.setBackground(Color.WHITE);
			pnSimple.setLayout(new MigLayout("", "[]", "[]"));
			generateQueryPanels(pnSimple, Dificultad.BASICA);
		}
		return pnSimple;
	}
	private JScrollPane getSpIntermedio() {
		if (spIntermedio == null) {
			spIntermedio = new JScrollPane();
			spIntermedio.setViewportView(getPnIntermedias());
		}
		return spIntermedio;
	}
	private JScrollPane getSpAvanzado() {
		if (spAvanzado == null) {
			spAvanzado = new JScrollPane();
			spAvanzado.setViewportView(getPnAvanzadas());
		}
		return spAvanzado;
	}
	private JPanel getPnIntermedias() {
		if (pnIntermedias == null) {
			pnIntermedias = new JPanel();
			pnIntermedias.setBackground(Color.WHITE);
			pnIntermedias.setLayout(new MigLayout("", "[]", "[]"));
			generateQueryPanels(pnIntermedias, Dificultad.INTERMEDIA);
		}
		return pnIntermedias;
	}
	private JPanel getPnAvanzadas() {
		if (pnAvanzadas == null) {
			pnAvanzadas = new JPanel();
			pnAvanzadas.setBackground(Color.WHITE);
			generateQueryPanels(pnAvanzadas, Dificultad.AVANZADA);
			pnAvanzadas.setLayout(new MigLayout("", "[]", "[]"));
		}
		return pnAvanzadas;
	}
	
	private void generateQueryPanels(JPanel panel, Dificultad dificultad)
	{
		int rowIndex = 0;
		for(Consulta c : neo4j.consultasPreProgramas )
		{
			if( !c.getDificultad().equals(dificultad) )
				continue;
			JButton btn = new JButton( c.getNombreConsulta() );
			btn.addActionListener( listenerConsulta );
			JLabel lbl = new JLabel( c.getDescripcion() );
			
			panel.add( btn, "Cell 0 " + rowIndex );
			panel.add( lbl, "Cell 1 " + rowIndex );
			rowIndex++;
		}
		
	}
	
	class ListenerConsulta implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton btn = (JButton) arg0.getSource();
			String nombreConsulta = btn.getText();
			Consulta consulta = neo4j.consultasPreProgramas.stream().filter( x -> x.getNombreConsulta().equals( nombreConsulta ) ).collect(Collectors.toList()).get(0);
			String output = neo4j.premadeQuery( consulta.getQuery() );
			
			txManualQuery.setText( consulta.getQuery() );
			panelQueryResult.addOutput(consulta.getQuery(), output);
		}
		
	}
}
