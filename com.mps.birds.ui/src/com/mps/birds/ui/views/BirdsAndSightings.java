package com.mps.birds.ui.views;

import java.time.LocalDateTime;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import com.mps.birds.core.BirdDto;
import com.mps.birds.core.SightingDto;
import com.mps.birds.ui.service.BirdService;
import com.mps.birds.ui.service.SightingService;

public class BirdsAndSightings extends ViewPart {

	public static final String ID = "com.mps.birds.ui.views.BirdsAndSightings";

	private Table birdsTable;
	private Table sightingsTable;
	private BirdService birdService = new BirdService();
	private SightingService sightingService = new SightingService();

	private Text nameT;
	private Text colorT;
	private Text weightT;
	private Text heightT;
	private Text birdNameT;
	private Text locationT;
	private DateTime datePicker;
	private DateTime timePicker;

	@Override
	public void createPartControl(Composite parent) {
		// Main container
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));


		// --- BOTTOM: SIDE-BY-SIDE TABLES ---
		// Changed to SWT.HORIZONTAL for side by sid layout
		SashForm sashForm = new SashForm(mainComposite, SWT.HORIZONTAL);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Bird Table Container
		Composite leftComposite = new Composite(sashForm, SWT.NONE);
		leftComposite.setLayout(new GridLayout(1, false));
		createBirdInputFields(leftComposite);
		new Label(leftComposite, SWT.NONE).setText("Birds List:");
		buildBirdsTable(leftComposite);

		// Sightings Table Container
		Composite rightComposite = new Composite(sashForm, SWT.NONE);
		rightComposite.setLayout(new GridLayout(1, false));
		createSightingInputFields(rightComposite);
		new Label(rightComposite, SWT.NONE).setText("Sightings:");
		buildSightingsTable(rightComposite);

		sashForm.setWeights(new int[] { 50, 50 });

		attachListeners();
		populateBirdsInTable();
		populateSightings("");
	}

	private void createBirdInputFields(Composite parent) {
//		NAME
		new Label(parent, SWT.NONE).setText("Name:");
		nameT = new Text(parent, SWT.BORDER);
		nameT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//		COLOR
		new Label(parent, SWT.NONE).setText("Color:");
		colorT = new Text(parent, SWT.BORDER);
		colorT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//		WEIGHT
		new Label(parent, SWT.NONE).setText("Weight:");
		weightT = new Text(parent, SWT.BORDER);
		weightT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//		HEIGHT
		new Label(parent, SWT.NONE).setText("Height:");
		heightT = new Text(parent, SWT.BORDER);
		heightT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button addBtn = new Button(parent, SWT.PUSH);
		addBtn.setText("Add Bird...");
		addBtn.addListener(SWT.Selection, e -> {
			try {
		        double weight = Double.parseDouble(weightT.getText());
		        double height = Double.parseDouble(heightT.getText());
		        
		        birdService.addNewBird(nameT.getText(), colorT.getText(), weight, height);
		        populateBirdsInTable();
		    } catch (NumberFormatException nfe) {
		        showExceptionDialog(new Exception("Please enter valid numeric values for Weight and Height."));
		    } catch (Exception ex) {
		        showExceptionDialog(ex);
		    }
		});
		
		Button showAllBirdsBtn = new Button(parent, SWT.PUSH);
		showAllBirdsBtn.setText("Show all birds");
		showAllBirdsBtn.addListener(SWT.Selection, e -> {
			populateBirdsInTable();
		});
	}

	private void createSightingInputFields(Composite parent) {
//		BIRD NAME
		new Label(parent, SWT.NONE).setText("Bird name:");
		birdNameT = new Text(parent, SWT.BORDER);
		birdNameT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//      LOCATION
		new Label(parent, SWT.NONE).setText("Location:");
		locationT = new Text(parent, SWT.BORDER);
		locationT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//      DATE
		new Label(parent, SWT.NONE).setText("Date:");
		datePicker = new DateTime(parent, SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
		datePicker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

//		TIME
		new Label(parent, SWT.NONE).setText("Time:");
		timePicker = new DateTime(parent, SWT.TIME | SWT.SHORT | SWT.BORDER);
		timePicker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button addBtn = new Button(parent, SWT.PUSH);
		addBtn.setText("Add Sighting...");
		addBtn.addListener(SWT.Selection, e -> {

			try {
				sightingService.addNewSightSing(birdNameT.getText(), locationT.getText(),
						LocalDateTime.of(datePicker.getYear(), datePicker.getMonth(), datePicker.getDay(),
								timePicker.getHours(), timePicker.getMinutes()));
				populateSightings(birdNameT.getText());
			} catch (Exception ex) {
				ex.printStackTrace();
				showExceptionDialog(ex);
			}
		});

		Button filterByBirdNameBtn = new Button(parent, SWT.PUSH);
		filterByBirdNameBtn.setText("Filter By Bird Name");
		filterByBirdNameBtn.addListener(SWT.Selection, e -> {
			populateSightings(birdNameT.getText());
		});
	}

	private void attachListeners() {
		birdsTable.addListener(SWT.Selection, e -> {
			TableItem[] selection = birdsTable.getSelection();
			if (selection.length > 0) {
				populateSightings(selection[0].getText(0));
			}
		});
	}

	private void populateSightings(String birdName) {
		sightingsTable.removeAll();
		birdNameT.setText(birdName);
		try {
			List<SightingDto> sightings = sightingService.getSightings(birdName);
			for (SightingDto s : sightings) {
				TableItem item = new TableItem(sightingsTable, SWT.NONE);
				item.setText(new String[] { s.getBirdName(), s.getLocation(), s.getDate().toString() });
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			showExceptionDialog(ex);
		}
	}

	private void populateBirdsInTable() {
		birdsTable.removeAll();
		try {
			List<BirdDto> birds = birdService.getAllBirds();
			for (BirdDto b : birds) {
				TableItem item = new TableItem(birdsTable, SWT.NONE);
				item.setText(
						new String[] { b.getName(), b.getColor(), b.getWeight().toString(), b.getHeight().toString() });
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			showExceptionDialog(ex);
		}
	}

	private void showExceptionDialog(Exception ex) {
		Display.getDefault().asyncExec(() -> {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error", ex.getMessage());
		});
	}

	// TABLES

	private void buildBirdsTable(Composite parent) {
		birdsTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		birdsTable.setHeaderVisible(true);
		birdsTable.setLinesVisible(true);
		birdsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		String[] headers = { "Name", "Color", "Width", "Height" };
		for (String h : headers) {
			TableColumn col = new TableColumn(birdsTable, SWT.NONE);
			col.setText(h);
			col.setWidth(100);
		}
	}

	private void buildSightingsTable(Composite parent) {
		sightingsTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION);
		sightingsTable.setHeaderVisible(true);
		sightingsTable.setLinesVisible(true);
		sightingsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		String[] headers = { "Bird name", "Location", "Date/Time" };
		for (String h : headers) {
			TableColumn col = new TableColumn(sightingsTable, SWT.NONE);
			col.setText(h);
			col.setWidth(100);
		}
	}

	@Override
	public void setFocus() {
		birdsTable.setFocus();
	}
}