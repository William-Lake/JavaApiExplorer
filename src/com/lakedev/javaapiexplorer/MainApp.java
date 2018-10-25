package com.lakedev.javaapiexplorer;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application
{
	private ComboBox<JavaClass> cmbJavaClass;
	
	private TextArea txtClassDescription;
	
	private Hyperlink lnkJavaDoc;
	
	private Hyperlink lnkProgramCreek;

	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void init() throws Exception
	{
		
		Platform
		.runLater(() -> 
		{
			JavaApiExplorer
			.getInstance()
			.loadData();
		});
		
		super.init();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		VBox root = new VBox();
		
		HBox searchControlContainer = new HBox();
		
		searchControlContainer.setPadding(new Insets(5));
		
		cmbJavaClass = new ComboBox<>();
		
		HBox.setHgrow(cmbJavaClass, Priority.ALWAYS);
		
		cmbJavaClass.getItems().addAll(JavaApiExplorer.getInstance().getJavaClasses());
		
		Collections.sort(cmbJavaClass.getItems());
		
		cmbJavaClass.setOnAction((selectionMade) -> loadJavaClass(cmbJavaClass.getValue()));
		
		Button btnPickRandom = new Button("Random");
		
		btnPickRandom.setPrefWidth(100);
		
		btnPickRandom.setOnAction((clicked) -> pickRandomClass());
		
		searchControlContainer.getChildren().addAll(cmbJavaClass, btnPickRandom);
		
		txtClassDescription = new TextArea();
		
		txtClassDescription.setFont(Font.font(null, FontWeight.BOLD, 14));
		
		txtClassDescription.setEditable(false);
		
		txtClassDescription.setWrapText(true);
		
		VBox.setVgrow(txtClassDescription, Priority.ALWAYS);
		
		lnkJavaDoc = new Hyperlink();
		
		lnkProgramCreek = new Hyperlink();
		
		lnkJavaDoc.setOnAction((clicked) -> launchWebPage(lnkJavaDoc.getText()));
		
		lnkProgramCreek.setOnAction((clicked) -> launchWebPage(lnkProgramCreek.getText()));
		
		root.getChildren().addAll(searchControlContainer, txtClassDescription, lnkJavaDoc, lnkProgramCreek);
		
		primaryStage.setTitle("Java API Explorer");
		
		primaryStage.setScene(new Scene(root, 400, 250));
		
		primaryStage.setResizable(false);
		
		primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("JavaApiExplorerIcon.png")));
		
		primaryStage.show();
	}

	private void loadJavaClass(JavaClass javaClass)
	{
		txtClassDescription.setText(javaClass.getClassDescription());
		
		lnkJavaDoc.setText(javaClass.getJavaDocUrl());
		
		lnkJavaDoc.setVisited(false);
		
		lnkProgramCreek.setText(javaClass.getProgramCreekUrl());
		
		lnkProgramCreek.setVisited(false);
	}

	private void launchWebPage(String url)
	{
		try
		{
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	private void pickRandomClass()
	{
		JavaClass javaClass = 
				JavaApiExplorer
				.getInstance()
				.pickRandomClass();
		
		cmbJavaClass.setValue(javaClass);
		
		loadJavaClass(javaClass);
	}

}
