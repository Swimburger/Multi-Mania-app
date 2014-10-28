<?php
use Repositories\NewsRepository;
use Repositories\TalkRepository;
use Repositories\RoomRepository;
use Repositories\TagRepository;
use Repositories\UserRepository;
use Repositories\TalkTagRepository;
use Repositories\TalkSpeakerRepository;
use Repositories\SpeakerRepository;
use Data\Import;
use Utilities\Params;

require 'vendor/autoload.php';
require 'Slim/Slim.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();
$app->config('debug',!Utilities\Params::PRODUCTION);//if in production, debug is false else true

$app->get('/news', 'getNews');

$app->get('/talks', 'getTalks');
$app->get('/users/:id/talks', 'getTalksWithFavorites');
$app->get('/talk_tags', 'getTalkTags');
$app->get('/talk_speakers', 'getTalkSpeakers');

$app->get('/rooms', 'getRooms');

$app->get('/tags', 'getTags');

$app->get('/speakers', 'getSpeakers');

$app->get('/import/:secret','importData');

/**
 * inserts a user with the passed id
 */
$app->post('/users/:id',function ($id) use ($app){
    if(UserRepository::insertUser($id)){
        echo $id;
    }else{
        //check if user already exists and return it's id if so...
        UserRepository::getUserById($id);//untested
        $app->error();
    }
});

/**
 * Favorites a talk for the user
 */
$app->post('/users/:userid/talks/:talkid',function ($userid,$talkid) use ($app){
    if(UserRepository::insertFavorite($userid,$talkid)){
        echo 'success';
    }else{
        $app->error();
    }
});

/**
 * Unfavorites a talk for the user
 */
$app->delete('/users/:userid/talks/:talkid',function ($userid,$talkid) use ($app){
    if(UserRepository::removeFavorite($userid,$talkid)){
        echo 'success';
    }else{
        $app->error();
    }
});
$app->contentType('application/json');
$app->run();
/**
 * Gets news items from the database
 */
function getNews() {
    echo json_encode(NewsRepository::getNews());
}
/**
 * Gets talks from the database
 */
function getTalks() {
    echo json_encode(TalkRepository::getTalks());
}

/**
 * @param $userid id of a user (string)
 * returns all talks with a boolean to know if it's a favorite for that user
 */
function getTalksWithFavorites($userid){
    echo json_encode(TalkRepository::getTalksWithFavorites($userid));
}

/**
 * Gets the ids for the talks with the ids for tags
 */
function getTalkTags(){
    echo json_encode(TalkTagRepository::getTalkTags());
}

/**
 * Gets the ids for the talks with the ids for the speakers for that talk
 */
function getTalkSpeakers(){
    echo json_encode(TalkSpeakerRepository::getTalkSpeakers());
}

/**
 * Gets the rooms for the talks
 */
function getRooms() {
    echo json_encode(RoomRepository::getRooms());
}

/**
 * Gets the tags for the talks
 */
function getTags() {
    echo json_encode(TagRepository::getTags());
}

function getSpeakers(){
    echo json_encode(SpeakerRepository::getSpeakers());
}

function importData($secret){
    if($secret!=Params::SECRET)
        return;
    Import::ImportData();
}
