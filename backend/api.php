<?php
use Repositories\NewsRepository;
use Repositories\TalkRepository;
use Repositories\RoomRepository;
use Repositories\TagRepository;
use Repositories\UserRepository;

require 'vendor/autoload.php';
require 'Slim/Slim.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();
$app->config('debug',!Utilities\Params::PRODUCTION);//if in production, debug is false else true

$app->get('/news', 'getNews');
$app->get('/talks', 'getTalks');
$app->get('/rooms', 'getRooms');
$app->get('/tags', 'getTags');
$app->get('/users/:id/talks', 'getTalksWithFavorites');

$app->post('/users/:id',function ($id) use ($app){
    if(UserRepository::insertUser($id)){
        echo $id;
    }else{
        $app->error();
    }
});

$app->post('/users/:userid/talks/:talkid',function ($userid,$talkid) use ($app){
    if(UserRepository::insertFavorite($userid,$talkid)){
        echo 'success';
    }else{
        $app->error();
    }
});

$app->run();

function getNews() {
    echo json_encode(NewsRepository::getNews());
}
function getTalks() {
    echo json_encode(TalkRepository::getTalks());
}
function getTalksWithFavorites($userid){
    echo json_encode(TalkRepository::getTalksWithFavorites($userid));
}
function getRooms() {
    echo json_encode(RoomRepository::getRooms());
}
function getTags() {
    echo json_encode(TagRepository::getTags());
}