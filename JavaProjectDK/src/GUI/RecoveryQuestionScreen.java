package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class RecoveryQuestionScreen extends Application {
	String user = ForgotPasswordScreen.user;

	String secQuest = "";
	// string to hold security question answer once received from database
	String secAnswer = "";

	// string for user answer to security question to be compared to database entry
	String userAnswer = "";

	// string for password to be held once received from the database
	String password = "";

	public static void main(String[] args) {

		Application.launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Recovery Question");

		GridPane grid2 = new GridPane();
		grid2.setAlignment(Pos.TOP_LEFT);
		grid2.setHgap(10);
		grid2.setVgap(10);
		grid2.setPadding(new Insets(25, 25, 25, 25));
		
		Text scenetitle = new Text("Recovery Question");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid2.add(scenetitle, 1, 1);

		Scene recoveryQuestionScene = new Scene(grid2, 400, 200);

		Label securityQuestionLabel = new Label("Enter The Answer To Your Security Question: ");
		grid2.add(securityQuestionLabel, 1, 0);
		grid2.setAlignment(Pos.TOP_CENTER);
		securityQuestionLabel.setTextAlignment(TextAlignment.CENTER);

		Label questionLabel = new Label(secQuest);
		grid2.add(questionLabel, 1, 1);

		TextField answerField = new TextField();
		grid2.add(answerField, 1, 2);

		Button btnHome = new Button("Home");
		HBox hbBtnHome = new HBox(10);
		hbBtnHome.setAlignment(Pos.BOTTOM_LEFT);
		hbBtnHome.getChildren().add(btnHome);
		grid2.add(hbBtnHome, 1, 3);
		btnHome.setOnAction(e -> {
			try {
				LoginScreen screen = new LoginScreen();
				screen.start(primaryStage);
			} catch (Exception el) {
				el.printStackTrace();
			}
		});

		Button btnSubmit = new Button("Submit");
		HBox hbbtnSubmit = new HBox(10);
		hbbtnSubmit.setAlignment(Pos.BOTTOM_LEFT);
		hbbtnSubmit.getChildren().add(btnSubmit);
		grid2.add(hbbtnSubmit, 2, 3);
		btnSubmit.setOnAction(e -> {
			try {
				// get a connection to the database
				Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project_database_master",
						"root", "082486dk");
				// create a statement
				Statement myStat = myConn.createStatement();
				// execute a query
				ResultSet myRs;
			
				String sqlUserCheck = "SELECT UserInputAnswer, PasswordAsHash FROM Customer WHERE UserName = '" + user + "'";
				
				myRs = myStat.executeQuery(sqlUserCheck);
				

				// Creates a variable for future checking
				int count = 0;

				while (myRs.next()) {

					count++;

					// sets security answer from database to be compared to user answer
					secAnswer = myRs.getString("UserInputAnswer");

					// stores password from database
					password = myRs.getString("PasswordAsHash");
				}
				myStat.close();
				myRs.close();
				myConn.close();
				
				userAnswer = answerField.getText();

				if (userAnswer.equals(secAnswer)) {
					AlertBox.display("Password", "The password for your account is: " + password);

				} else if (secAnswer != userAnswer) {
					AlertBox.display("Incorrect Answer", "That answer is Incorrect. Please try again.");
				} else {
					AlertBox.display("Incorrect Answer", "That answer is Incorrect. Please try again.");
				}
			} catch (Exception el) {
				el.printStackTrace();
			}
		});

		primaryStage.setScene(recoveryQuestionScene);
		primaryStage.show();
		primaryStage.centerOnScreen();

	}

}
