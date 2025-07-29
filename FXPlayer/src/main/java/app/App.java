package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class App extends Application {

    private BorderPane root;
    private MediaPlayer playerAtual;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Visualizador de Mídia");
        root = new BorderPane();

        Button btnAbrir = new Button("Abrir Mídia");
        btnAbrir.setOnAction(e -> abrirArquivo(stage));

        HBox topBar = new HBox(10, btnAbrir);
        topBar.setStyle("-fx-padding: 10;");
        root.setTop(topBar);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void abrirArquivo(Stage stage) {
        pararMediaAtual();

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar Mídia");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Todos os Suportados", "*.txt", "*.jpg", "*.png", "*.jpeg", "*.mp3", "*.wav", "*.mp4"),
            new FileChooser.ExtensionFilter("Texto", "*.txt"),
            new FileChooser.ExtensionFilter("Imagem", "*.jpg", "*.png", "*.jpeg"),
            new FileChooser.ExtensionFilter("Áudio", "*.mp3", "*.wav"),
            new FileChooser.ExtensionFilter("Vídeo", "*.mp4")
        );

        File arquivo = chooser.showOpenDialog(stage);

        if (arquivo != null) {
            String nome = arquivo.getName().toLowerCase();
            try {
                if (nome.endsWith(".txt")) {
                    mostrarTexto(arquivo);
                } else if (nome.endsWith(".png") || nome.endsWith(".jpg") || nome.endsWith(".jpeg")) {
                    mostrarImagem(arquivo);
                } else if (nome.endsWith(".mp3") || nome.endsWith(".wav")) {
                    reproduzirAudio(arquivo);
                } else if (nome.endsWith(".mp4")) {
                    reproduzirVideo(arquivo);
                } else {
                    alert("Formato não suportado.");
                }
            } catch (Exception e) {
                alert("Erro: " + e.getMessage());
            }
        }
    }

    private void mostrarTexto(File arquivo) throws IOException {
        String conteudo = new String(java.nio.file.Files.readAllBytes(arquivo.toPath()));
        TextArea texto = new TextArea(conteudo);
        texto.setWrapText(true);
        root.setCenter(new ScrollPane(texto));
    }

    private void mostrarImagem(File arquivo) {
        Image img = new Image(arquivo.toURI().toString());
        ImageView imgView = new ImageView(img);
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(600);
        root.setCenter(new ScrollPane(imgView));
    }

    private void reproduzirAudio(File arquivo) {
        Media media = new Media(arquivo.toURI().toString());
        playerAtual = new MediaPlayer(media);
        playerAtual.play();
        alert("Reproduzindo áudio...");
    }

    private void reproduzirVideo(File arquivo) {
        Media media = new Media(arquivo.toURI().toString());
        playerAtual = new MediaPlayer(media);
        MediaView mediaView = new MediaView(playerAtual);
        mediaView.setFitWidth(600);
        mediaView.setPreserveRatio(true);
        root.setCenter(mediaView);
        playerAtual.play();
    }

    private void pararMediaAtual() {
        if (playerAtual != null) {
            playerAtual.stop();
            playerAtual.dispose();
            playerAtual = null;
        }
    }

    private void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
