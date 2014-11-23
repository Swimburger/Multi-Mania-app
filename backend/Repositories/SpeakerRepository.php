<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 18/10/2014
 * Time: 18:00
 */

namespace Repositories;


use Utilities\Utilities;

/**
 * The repository contains all methods for interacting with the database for the Speaker model
 *
 * Class SpeakerRepository
 * @package Repositories
 */
class SpeakerRepository {
    /**
     * @return array Returns all speaker
     */
    public static function  getSpeakers()
    {
        $sql_query = "SELECT * FROM speaker";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $id int The id of the speaker
     * @return mixed|null Returns the speaker if found, else returns null
     */
    public static function getSpeakerById($id)
    {
        $sql_query = "SELECT * FROM speaker WHERE id=:id;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    /**
     * @param $id int The id of the speaker
     * @param $name string the name of the speaker
     * @return bool Returns true if successful
     */
    public static function insertSpeaker($id, $name)
    {
        $sql_query = "INSERT INTO speaker VALUES (:id,:name);";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

    /**
     * @param $id int The id of the speaker
     * @param $name string The name of the speaker
     * @return bool Return true if successful
     */
    public static function updateSpeaker($id, $name)
    {
        $sql_query = "UPDATE speaker SET name=:name WHERE id=:id;";
        $con = Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':name'=>$name,':id'=>$id));
    }
} 