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
 * The repository contains all methods for interacting with the database for the TalkTag model
 *
 * Class TalkTagRepository
 * @package Repositories
 */
class TalkTagRepository {
    /**
     * @return array Returns all talktags
     */
    public static function  getTalkTags()
    {
        $sql_query = "SELECT * FROM talk_tag";
        $con=Utilities::getConnection();
        $stmt   = $con->query($sql_query);
        return $stmt->fetchAll(\PDO::FETCH_ASSOC);
    }

    /**
     * @param $talkId int The talkid of the TalkTag
     * @param $tagId int The tagid of the TalkTag
     * @return bool Returns true if successful
     */
    public static function insertTalkTag($talkId, $tagId)
    {
        try {
            $sql_query = "INSERT INTO talk_tag (talk_id, tag_id) VALUES (:talk_id,:tag_id);";
            $con = Utilities::getConnection();
            $stmt = $con->prepare($sql_query);
            return $stmt->execute(array(':talk_id' => $talkId, ':tag_id' => $tagId));
        }catch (\Exception $ex){
            return false;
        }
    }
} 