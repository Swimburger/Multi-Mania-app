<?php
use Repositories\NewsRepository;

require 'vendor/autoload.php';
require 'Slim/Slim.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();
$app->get('/news', 'getNews');
$app->get('/talks', 'getTalks');
$app->get('/rooms', 'getRooms');
$app->get('/tags', 'getTags');
$app->run();

function getNews() {
    echo json_encode(NewsRepository::getNews());
}
function getTalks() {
    echo json_encode(NewsRepository::getNews());//todo
}
function getRooms() {
    echo json_encode(NewsRepository::getNews());//todo
}
function getTags() {
    echo json_encode(NewsRepository::getNews());//todo
}

?>