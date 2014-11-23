<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:08
 */

namespace Repositories;

use Utilities\Utilities;


/**
 * The repository contains all methods for interacting with the database for the Tag model
 *
 * Class TagRepository
 * @package Repositories
 */
class TagRepository{

    /**
     * @return array Return all tags
     */
    public static function getTags(){
        $sql_query = "SELECT * FROM tag";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $id int The id of the tag
     * @return mixed|null Returns the tag if found, else return null
     */
    public static function getTagById($id)
    {
        $sql_query = "SELECT * FROM tag WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        if($stmt->execute(array(':id'=>$id))){
            return $stmt->fetch(\PDO::FETCH_ASSOC);
        }
        return null;
    }

    /**
     * @param $id int The id of the tag
     * @param $name string The name of the tag
     * @return bool Returns true if successful
     */
    public static function insertTag($id, $name)
    {
        $sql_query = "INSERT INTO tag VALUES (:id,:name);";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

    /**
     * @param $id int The id of the tag
     * @param $name string the name of the tag
     * @return bool Returns true if successful
     */
    public static function updateTag($id, $name)
    {
        $sql_query = "UPDATE tag SET name=:name WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':name'=>$name,':id'=>$id));
    }


}