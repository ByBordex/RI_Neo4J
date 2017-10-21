package GUI;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class JPanelQueryResult extends JPanel {
	private JTextPane txQueryOutput;
	private JButton btnClear;
	private JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public JPanelQueryResult() {
		setBorder(new TitledBorder(null, "Resultados de la consulta:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setBackground(Color.WHITE);
		setLayout(new MigLayout("", "[grow]", "[grow][]"));
		add(getScrollPane_1(), "cell 0 0,grow");
		add(getBtnClear(), "cell 0 1,growx");

	}

	public void addOutput(String query, String output)
	{
		String separator = "\n------- " + Calendar.getInstance().getTime() + " -------\n";
		String consulta = "    Consulta:    " + query + "\n";
		String current = txQueryOutput.getText();
		
		txQueryOutput.setText(  current + separator + consulta + output);
		
	}
	
	private JTextPane getTxQueryOutput() {
		if (txQueryOutput == null) {
			txQueryOutput = new JTextPane();
			txQueryOutput.setEditable(false);
		}
		return txQueryOutput;
	}
	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton("Limpiar salida");
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getTxQueryOutput().setText( "" );
				}
			});
		}
		return btnClear;
	}
	private JScrollPane getScrollPane_1() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTxQueryOutput());
		}
		return scrollPane;
	}
}
