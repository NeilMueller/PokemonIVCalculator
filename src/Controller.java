import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField pokemonIDTF;
    @FXML
    private TextField pokemonNameTF;
    @FXML
    private Label idealStatout;
    @FXML
    private Label worseStatout;
    @FXML
    private TextField pokemonlvlTF;
    @FXML
    private TextField pokemonNatureTF;

    @FXML
    private TextField hpIVTF;
    @FXML
    private TextField atkIVTF;
    @FXML
    private TextField defIVTF;
    @FXML
    private TextField spAtkIVTF;
    @FXML
    private TextField spDefIVTF;
    @FXML
    private TextField spdIVTF;

    @FXML
    private TextField hpEVTF;
    @FXML
    private TextField atkEVTF;
    @FXML
    private TextField defEVTF;
    @FXML
    private TextField spAtkEVTF;
    @FXML
    private TextField spDefEVTF;
    @FXML
    private TextField spdEVTF;

    @FXML
    private Label hpStatOut;
    @FXML
    private Label atkStatOut;
    @FXML
    private Label defStatOut;
    @FXML
    private Label spAtkStatOut;
    @FXML
    private Label spDefStatOut;
    @FXML
    private Label spdStatOut;
    @FXML
    private Label oddOut;

    private Calculator calculator;

    @FXML
    private void initialize(){
        calculator = new Calculator();
    }

    @FXML
    private void calculate(ActionEvent event){
        event.consume();

        if (validateInfo()) {

            loadPokemon();
            loadIVs();
            loadEVs();

            calculator.calculate();
            pokemonIDTF.setText(String.valueOf(calculator.getId()));
            String[] strings = calculator.getStrings();
            pokemonNameTF.setText(strings[0]);
            idealStatout.setText(strings[1]);
            worseStatout.setText(strings[2]);

            displayResults(calculator.getOut());
        }

    }

    private void loadPokemon(){
        calculator.setName(pokemonNameTF.getText());
        if(!pokemonIDTF.getText().isEmpty()) {
            calculator.setId(Integer.parseInt(pokemonIDTF.getText()));
        }
        calculator.setNature(pokemonNatureTF.getText());
        calculator.setLevel(Integer.parseInt(pokemonlvlTF.getText()));
    }

    private void loadIVs(){
        int[] ivs = new int[6];
        ivs[0] = Integer.parseInt(hpIVTF.getText());
        ivs[1] = Integer.parseInt(atkIVTF.getText());
        ivs[2] = Integer.parseInt(defIVTF.getText());
        ivs[3] = Integer.parseInt(spAtkIVTF.getText());
        ivs[4] = Integer.parseInt(spDefIVTF.getText());
        ivs[5] = Integer.parseInt(spdIVTF.getText());

        calculator.setIvs(ivs);
    }

    private void loadEVs(){
        int[] evs = new int[6];
        evs[0] = Integer.parseInt(hpEVTF.getText());
        evs[1] = Integer.parseInt(atkEVTF.getText());
        evs[2] = Integer.parseInt(defEVTF.getText());
        evs[3] = Integer.parseInt(spAtkEVTF.getText());
        evs[4] = Integer.parseInt(spDefEVTF.getText());
        evs[5] = Integer.parseInt(spdEVTF.getText());

        calculator.setEvs(evs);
    }

    private void displayResults(int[] out){
        hpStatOut.setText( Integer.toString(out[0]));
        atkStatOut.setText( Integer.toString(out[1]));
        defStatOut.setText( Integer.toString(out[2]));
        spAtkStatOut.setText( Integer.toString(out[3]));
        spDefStatOut.setText( Integer.toString(out[4]));
        spdStatOut.setText( Integer.toString(out[5]));
        oddOut.setText(Double.toString(calculator.getProb()));
        System.out.println(calculator.getProb());
    }

    private void displayError(String errorMsg){
        AlertBox.display(errorMsg);
    }

    private boolean validateInfo(){

        if(missingField()) {
            displayError("Missing Fields");
            return false;
        }
        if(!inputIsInt()) {
            displayError("Enter number");
            return false;
        }
        if(invalidIVRange()) {
            displayError("IV must be in range 0 - 31");
            return false;
        }
        if(invalidEVRange()) {
            displayError("EV must be in range 0 - 252");
            return false;
        }
        if(invalidEVTotal()){
            displayError("EV total must be below 510");
            return false;
        }

        return true;

    }

    private boolean missingField(){
        if(pokemonIDTF.getText().isEmpty() && pokemonNameTF.getText().isEmpty()) return true;
        if(fieldEmpty(pokemonlvlTF)) return true;
        if(fieldEmpty(pokemonNatureTF)) return true;

        if(fieldEmpty(hpIVTF)) return true;
        if(fieldEmpty(atkIVTF)) return true;
        if(fieldEmpty(defIVTF)) return true;
        if(fieldEmpty(spAtkIVTF)) return true;
        if(fieldEmpty(spDefIVTF)) return true;
        if(fieldEmpty(spdIVTF)) return true;

        if(fieldEmpty(hpEVTF)) return true;
        if(fieldEmpty(atkEVTF)) return true;
        if(fieldEmpty(defEVTF)) return true;
        if(fieldEmpty(spAtkEVTF)) return true;
        if(fieldEmpty(spDefEVTF)) return true;
        if(fieldEmpty(spdEVTF)) return true;

        return false;
    }

    private boolean fieldEmpty(TextField textField){
        if(textField.getText().isEmpty()){
            textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            return true;
        } else {
            textField.setStyle(null);
            return false;
        }
    }

    private boolean inputIsInt(){

        if(!pokemonIDTF.getText().isEmpty()) {
            if (!isInt(pokemonIDTF)) return false;
        }

        if(!isInt(pokemonlvlTF)) return false;

        if(!isInt(hpIVTF)) return false;
        if(!isInt(atkIVTF)) return false;
        if(!isInt(defIVTF)) return false;
        if(!isInt(spAtkIVTF)) return false;
        if(!isInt(spDefIVTF)) return false;
        if(!isInt(spdIVTF)) return false;

        if(!isInt(hpEVTF)) return false;
        if(!isInt(atkEVTF)) return false;
        if(!isInt(defEVTF)) return false;
        if(!isInt(spAtkEVTF)) return false;
        if(!isInt(spDefEVTF)) return false;
        if(!isInt(spdEVTF)) return false;

        return true;
    }

    private boolean isInt(TextField textField){
        try {
            int num = Integer.parseInt(textField.getText());
            textField.setStyle(null);
            return true;

        } catch (NumberFormatException e){
            textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            return false;
        }
    }

    private boolean invalidIVRange(){

        if(!validateIV(hpIVTF)) return true;
        if(!validateIV(atkIVTF)) return true;
        if(!validateIV(defIVTF)) return true;
        if(!validateIV(spAtkIVTF)) return true;
        if(!validateIV(spDefIVTF)) return true;
        if(!validateIV(spdIVTF)) return true;

        return false;
    }

    private boolean validateIV(TextField textField){
        int iv =  Integer.parseInt(textField.getText());
        if(iv >= 0 && iv < 32){
            textField.setStyle(null);
            return true;
        } else {
            textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            return false;
        }
    }

    private boolean invalidEVRange(){

        if(!validateEV(hpEVTF)) return true;
        if(!validateEV(atkEVTF)) return true;
        if(!validateEV(defEVTF)) return true;
        if(!validateEV(spAtkEVTF)) return true;
        if(!validateEV(spDefEVTF)) return true;
        if(!validateEV(spdEVTF)) return true;

        return false;
    }

    private boolean validateEV(TextField textField){
        int ev =  Integer.parseInt(textField.getText());
        if(ev <= 0 && ev < 253){
            textField.setStyle(null);
            return true;
        } else {

            textField.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
            return false;
        }
    }

    private boolean invalidEVTotal(){
        int sum = 0;
        sum += Integer.parseInt(hpEVTF.getText());
        sum += Integer.parseInt(atkEVTF.getText());
        sum += Integer.parseInt(defEVTF.getText());
        sum += Integer.parseInt(spAtkEVTF.getText());
        sum += Integer.parseInt(spDefEVTF.getText());
        sum += Integer.parseInt(spdEVTF.getText());

        if(sum > 510){
            return true;
        } else {
            return false;
        }
    }

}
