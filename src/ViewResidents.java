import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class ViewResidents {

	private JFrame frame;
	private JTextField inputName;
	
	@SuppressWarnings("rawtypes")
	private JComboBox optSex, optProvince, optFromYear, optFromSem, optToYear, optToSem, optScholar, optHall;
	
	private final String[] OPTIONS_PROVINCES = new String[] {"", "Abra", "Agusan del Norte", "Agusan del Sur", "Aklan", "Albay", "Antique", "Apayao", "Aurora", "Basilan", "Bataan", "Batanes", "Batangas", "Benguet", "Biliran", "Bohol", "Bukidnon", "Bulacan", "Cagayan", "Camarines Norte", "Camarines Sur", "Camiguin", "Capiz", "Catanduanes", "Cavite", "Cebu", "Compostela Valley", "Cotabato", "Davao del Norte", "Davao del Sur", "Davao Occidental", "Davao Oriental", "Dinagat Islands", "Eastern Samar", "Guimaras", "Ifugao", "Ilocos Norte", "Ilocos Sur", "Iloilo", "Isabela", "Kalinga", "La Union", "Laguna", "Lanao del Norte", "Lanao del Sur", "Leyte", "Maguindanao", "Marinduque", "Masbate", "Misamis Occidental", "Misamis Oriental", "Mountain Province", "Negros Occidental", "Negros Oriental", "Northern Samar", "Nueva Ecija", "Nueva Vizcaya", "Occidental Mindoro", "Oriental Mindoro", "Palawan", "Pampanga", "Pangasinan", "Quezon", "Quirino", "Rizal", "Romblon", "Samar", "Sarangani", "Siquijor", "Sorsogon", "South Cotabato", "Southern Leyte", "Sultan Kudarat", "Sulu", "Surigao del Norte", "Surigao del Sur", "Tarlac", "Tawi-Tawi", "Zambales", "Zamboanga del Norte", "Zamboanga del Sur", "Zamboanga Sibugay", "Metro Manila"};
	private final String[] OPTIONS_SEMESTERS = new String[] {"", "Intersession/Summer", "First Semester", "Second Semester"};
	private final String[] OPTIONS_YEARS = new String[] {"", "2010", "2011", "2012", "2013", "2014", "2015", "2016"};
	private final String[] OPTIONS_BULKACTIONS = new String[] {"Delete", "Email", "Text", "Export"};
	private final String[] OPTIONS_HALLS = new String[] {"", "Cervini", "Eliazo", "UD North", "UD South"};
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewResidents window = new ViewResidents();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ViewResidents() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new DBWindow();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		JSplitPane splitPane = initializeSearchTab();
		JPanel addPanel = initializeAddTab();
		
		tabbedPane.addTab("Search", null, splitPane, null);
		tabbedPane.addTab("Add/Upload", null, addPanel, null);		
	}
	
	@SuppressWarnings("serial")
	private class DBWindow extends JFrame implements WindowListener{
		public DBWindow(){
			this.setBounds(100, 100, 1064, 700);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.addWindowListener(this);
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			DBConnector.setup();
		}
		
		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			DBConnector.shutdown();
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}
		
		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JSplitPane initializeSearchTab() {
		SearchListener searchLstnr = new SearchListener();
		JSplitPane splitPane = new JSplitPane();
		
		JPanel filterPanel = new JPanel();
		splitPane.setLeftComponent(filterPanel);
		filterPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("max(150px;default):grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblName = new JLabel("Name");
		filterPanel.add(lblName, "2, 2, left, center");
		
		inputName = new JTextField();
		filterPanel.add(inputName, "2, 4, fill, default");
		inputName.setColumns(10);
		
		JLabel lblGender = new JLabel("Sex");
		filterPanel.add(lblGender, "2, 6");
		
		optSex = new JComboBox();
		optSex.setModel(new DefaultComboBoxModel(new String[] {"", "Male", "Female"}));
		filterPanel.add(optSex, "2, 8, fill, default");
		
		JLabel lblProvince = new JLabel("Province");
		filterPanel.add(lblProvince, "2, 10");
		
		optProvince = new JComboBox();
		optProvince.setEditable(true);
		optProvince.setModel(new DefaultComboBoxModel(OPTIONS_PROVINCES));
		filterPanel.add(optProvince, "2, 12, fill, default");
		
		JLabel lblFrom = new JLabel("From");
		filterPanel.add(lblFrom, "2, 14");
		
		optFromYear = new JComboBox();
		optFromYear.setModel(new DefaultComboBoxModel(OPTIONS_YEARS));
		optFromYear.setEditable(true);
		filterPanel.add(optFromYear, "2, 16, fill, default");
		
		optFromSem = new JComboBox();
		optFromSem.setModel(new DefaultComboBoxModel(OPTIONS_SEMESTERS));
		filterPanel.add(optFromSem, "2, 18, fill, default");
		
		JLabel lblTo = new JLabel("To");
		filterPanel.add(lblTo, "2, 20");
		
		optToYear = new JComboBox();
		optToYear.setEditable(true);
		optToYear.setModel(new DefaultComboBoxModel(OPTIONS_YEARS));
		filterPanel.add(optToYear, "2, 22, fill, default");
		
		optToSem = new JComboBox();
		optToSem.setModel(new DefaultComboBoxModel(OPTIONS_SEMESTERS));
		filterPanel.add(optToSem, "2, 24, fill, default");
		
		JLabel lblDormScholar = new JLabel("Dorm Scholar?");
		filterPanel.add(lblDormScholar, "2, 26");
		
		optScholar = new JComboBox();
		optScholar.setModel(new DefaultComboBoxModel(new String[] {"", "Yes", "No"}));
		filterPanel.add(optScholar, "2, 28, fill, default");
		
		JLabel lblHall = new JLabel("Hall");
		filterPanel.add(lblHall, "2, 30");
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(searchLstnr);
		
		optHall = new JComboBox();
		optHall.setModel(new DefaultComboBoxModel(OPTIONS_HALLS));
		filterPanel.add(optHall, "2, 32, fill, default");
		filterPanel.add(btnSearch, "2, 36");
		
		JPanel resultsPanel = new JPanel();
		splitPane.setRightComponent(resultsPanel);
		resultsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(300px;default):grow")},
			new RowSpec[] {
				RowSpec.decode("31px"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("441px"),}));

		JPanel actionPanel = new JPanel();
//		resultsPanel.add(actionPanel, "1, 1, right, top");
		actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblBulkAct = new JLabel("Bulk Actions");
		actionPanel.add(lblBulkAct);
		
		JComboBox optBulkAct = new JComboBox(new DefaultComboBoxModel(OPTIONS_BULKACTIONS));
		actionPanel.add(optBulkAct);
		
		JButton btnBulkAct = new JButton("Go");
		actionPanel.add(btnBulkAct);

		JTable resultsTable = new JTable();
		resultsTable.setFillsViewportHeight(true);
		searchLstnr.setResults(resultsTable);
		
		resultsTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Last Name", "Given Name", "Middle Initial", "Course", "Address", "Birthday",
				"Gender", "Scholar?"
			}
		));
		
		JScrollPane scrollPane = new JScrollPane(resultsTable);
		scrollPane.setViewportBorder(null);
		resultsPanel.add(scrollPane, "1, 3, fill, top");
		
		return splitPane;
	}
	
	public String convertSemToNum(String sem){
		if(sem.equals("Intersession/Summer"))
			return "0";
		else if(sem.equals("First Semester"))
			return "1";
		else if(sem.equals("Second Semester"))
			return "2";
		else
			return "-1";
	}

	private JPanel initializeAddTab() {
		JPanel addPanel = new JPanel();
		addPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblUploadExcelFile = new JLabel("Upload a directory");
		addPanel.add(lblUploadExcelFile);
		lblUploadExcelFile.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnSelectExcelFile = new JButton("Select Excel File");
		addPanel.add(btnSelectExcelFile);

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		btnSelectExcelFile.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent ae){
				int returnVal = fileChooser.showOpenDialog(addPanel);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                //Read file
	            } else {
	            }
			}			
		});
		
		return addPanel;
	}
	
	private class SearchListener implements ActionListener{
		private JTable results;
		private String name, course, address, province, bday, sex, isScholar, hall, fromYr, fromSem, toYr, toSem;
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			this.setName(inputName.getText());
			this.setSex((String) optSex.getSelectedItem());
			this.setProvince((String) optProvince.getSelectedItem());
			this.setHall((String) optHall.getSelectedItem()); 
			this.setFromYr((String) optFromYear.getSelectedItem());
			this.setFromSem(convertSemToNum((String) optFromSem.getSelectedItem()));
			this.setToYr((String) optToYear.getSelectedItem());
			this.setToSem(convertSemToNum((String) optToSem.getSelectedItem()));
			this.setScholar((String) optScholar.getSelectedItem());
			
			String[] queries = { name, course, address, province, bday, sex, isScholar, hall, fromYr, fromSem, toYr, toSem };
			ArrayList<Resident> residents = null;
			try {
				residents = DBConnector.queryResidents(queries);

				DefaultTableModel tm = (DefaultTableModel) results.getModel();
				tm.setRowCount(0);

				for(Resident r : residents){
					tm.addRow(getResidentArray(r));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String[] getResidentArray(Resident r){
			String scho = r.isScholar ? "Yes" : "No";
			
			return new String[]{r.lastName, r.firstName, r.middleInitial, r.course, 
								r.address, r.birthday, r.gender, scho};
		}
		
		public void setResults(JTable jt){
			this.results = jt;
		}
		
		public void setName(String name) {
			if(name.equals("")) this.name = null;
			else this.name = name;
		}

		public void setSex(String sex) {
			if(sex.equals("")) this.sex = null;
			else this.sex = sex;
		}

		public void setProvince(String province) {
			if(province.equals("")) this.province = null;
			else this.province = province;
		}

		public void setFromYr(String fromYr) {
			if(fromYr.equals("")) this.fromYr = null;
			else this.fromYr = fromYr;
		}

		public void setFromSem(String fromSem) {
			if(fromSem.equals("")) this.fromSem = null;
			else this.fromSem = fromSem;
		}

		public void setToYr(String toYr) {
			if(toYr.equals("")) this.toYr = null;
			else this.toYr = toYr;
		}

		public void setToSem(String toSem) {
			if(toSem.equals("")) this.toSem = null;
			else this.toSem = toSem;
		}

		public void setScholar(String isScholar) {
			if(isScholar.equals("")) this.isScholar = null;
			else if(isScholar.equals("Yes")) this.isScholar = "true";
			else this.isScholar = "false";
		}
		
		public void setHall(String hall) {
			if(hall.equals("")) this.hall = null;
			else if(hall.equals("Cervini")) this.hall = "CH";
			else if(hall.equals("Eliazo")) this.hall = "EH";
			else if(hall.equals("UD North")) this.hall = "UDN";
			else if(hall.equals("UD South")) this.hall = "UDS";
		}
	}
}
