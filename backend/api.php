<?php
require 'Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();
$app->get('/users', 'getUsers'); // Using Get HTTP Method and process getUsers function
$app->get('/users/:id',    'getUser');
$app->run();


function getUsers() {
    $sql_query = "SELECT id FROM User";
    try {
        $dbCon = connect_db();
        $stmt   = $dbCon->query($sql_query);
        $users  = $stmt->fetchAll(PDO::FETCH_OBJ);
        $dbCon = null;
        echo '{"users": ' . json_encode($users) . '}';
    }
    catch(PDOException $e) {
        echo '{"error":{"text":'. $e->getMessage() .'}}';
    }    
}



function connect_db() {
    try{
	    $server = 'http://student.howest.be/mysqlstudent';
	    $user = 'axeljonckheereMySQL';
	    $pass = 'Apo8aisool0j';
	    $database = 'axeljonckheereMySQL';
        $conn = new PDO('mysql:host='.$server.';dbname='.$database, $user, $pass);
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    }catch(PDOException $e){
        echo 'ERROR: '.$e->getMessage();
    }
	return $conn;
}

?>