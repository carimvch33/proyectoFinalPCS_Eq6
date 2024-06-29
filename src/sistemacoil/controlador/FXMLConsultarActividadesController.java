package sistemacoil.controlador;

import sistemacoil.modelo.dao.ColaboracionDAO;
import sistemacoil.modelo.pojo.Colaboracion;
import sistemacoil.utilidades.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class FXMLConsultarActividadesController implements Initializable {

    @FXML
    private TableView<Colaboracion> tvColaboracion;
    @FXML
    private TableColumn colColaboracion;
    
    private ObservableList<Colaboracion> colaboraciones;
    private ColaboracionDAO colaboracionDAO;
    private Stage primaryStage;
    @FXML
    private AnchorPane apVentana;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colaboracionDAO = new ColaboracionDAO();
        colColaboracion.setCellValueFactory(new PropertyValueFactory<>("nombreColaboracion"));
        cargarDatos();
    }    

    
    private void cargarDatos() {
        tvColaboracion.getItems().addAll(colaboracionDAO.obtenerActividadesConEvidencia());
    }

    @FXML
     private void btnSiguiente() {
        Colaboracion colaboracionSeleccionada = tvColaboracion.getSelectionModel().getSelectedItem();
        if (colaboracionSeleccionada != null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Seleccione una carpeta");
            File directorioDestino = directoryChooser.showDialog(primaryStage);
            if (directorioDestino != null) {
                try {
                    String ruta = directorioDestino.getAbsolutePath() + "/" + colaboracionSeleccionada.getNombreColaboracion() + "Actividad.pdf";
                    File archivoDestino = new File(ruta);
                    byte[] contenido = colaboracionDAO.obtenerContenidoEvidencia(colaboracionSeleccionada.getIdColaboracion());
                    if (contenido != null) {
                        OutputStream outputStream = new FileOutputStream(archivoDestino);
                        outputStream.write(contenido);
                        outputStream.close();
                       Utils.mostrarAlertaSimple("Descarga Exitosa", "La actividad fue descargada correctamente.", apVentana);
                    } else {
                        Utils.mostrarAlertaSimple("Error", "No se encontró evidencia para esta colaboración.", apVentana);
                    }
                } catch (IOException e) {
                    Utils.mostrarAlertaSimple("Error al exportar", e.getMessage(), apVentana);
                }
            }
        } else {
           Utils.mostrarAlertaSimple("Error", "Seleccione una colaboración de la tabla.", apVentana);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
   
    
    
}
