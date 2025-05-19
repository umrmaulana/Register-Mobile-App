<?php
$BASE_URL_IMAGES = "./images/";
$filename = "img" . date("YmdHis") . rand(9, 999) . ".jpg";

// Database connection
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "your_database_name"; // Replace with your database name

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$res = array();
$kode = "";
$pesan = "";

if ($_SERVER['REQUEST_METHOD'] == "POST") {
    if ($_FILES['imageupload']) {
        $temp_name = $_FILES['imageupload']['tmp_name'];
        $dest = $BASE_URL_IMAGES . $filename;
        
        // Move the uploaded image to the destination folder
        if (move_uploaded_file($temp_name, $dest)) {
            // Prepare an SQL query to insert image details into the database
            $sql = "INSERT INTO images (filename, path) VALUES ('$filename', '$dest')";

            if ($conn->query($sql) === TRUE) {
                $kode = 1;
                $pesan = "Upload Sukses dan data disimpan ke database";
            } else {
                $kode = 0;
                $pesan = "Error: " . $conn->error;
            }
        } else {
            $kode = 0;
            $pesan = "Upload Gagal";
        }
    } else {
        $kode = 0;
        $pesan = "Upload Gagal";
    }
} else {
    $kode = 0;
    $pesan = "Upload Gagal";
}

$res['kode'] = $kode;
$res['pesan'] = $pesan;

echo json_encode($res);

$conn->close();
