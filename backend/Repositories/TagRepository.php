<?php
/**
 * Created by PhpStorm.
 * User: Axel
 * Date: 20/10/2014
 * Time: 11:08
 */

namespace Repositories;

use Utilities\Utilities;

class TagRepository{

    /**
     * @return array
     */
    public static function getTags(){
        $sql_query = "SELECT * FROM tag";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

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

    public static function insertTag($id, $name)
    {
        $sql_query = "INSERT INTO tag VALUES (:id,:name);";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':id'=>$id,':name'=>$name));
    }

    public static function updateTag($id, $name)
    {
        $sql_query = "UPDATE tag SET name=:name WHERE id=:id;";
        $con=Utilities::getConnection();
        $stmt = $con->prepare($sql_query);
        return $stmt->execute(array(':name'=>$name,':id'=>$id));
    }


}