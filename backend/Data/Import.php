<?php
/**
 * Created by PhpStorm.
 * User: Niels
 * Date: 26/10/2014
 * Time: 17:27
 */

namespace Data;


use Repositories\RoomRepository;
use Repositories\TalkTagRepository;
use Repositories\TalkSpeakerRepository;
use Repositories\TagRepository;
use Repositories\SpeakerRepository;
use Repositories\TalkRepository;
use Repositories\NewsRepository;

/**
 * This class contains methods to import data from xml to the database
 *
 * Class Import
 * @package Data
 */
class Import {
    /**
     * If set to false, imports will fail be ignored if someone browses to /import
     * Use this constant to quickly disable the functionality online
     */
    const IMPORTALLOWED=true;

    /**
     * Parses the xml found in /Data/data.xml and inserts or updates them in the database
     */
    public static function ImportData(){
        if(!Import::IMPORTALLOWED)
        {
            return null;
        }

        $xml = simplexml_load_file("Data/data.xml");
        Import::InsertRooms($xml->rooms->room);
        Import::InsertTags($xml->tags->tag);
        Import::InsertSpeakers($xml->speakers->speaker);
        Import::InsertTalks($xml->talks->talk);
        Import::InsertTalkTags($xml->talk_tags->talk_tag);
        Import::InsertTalkSpeakers($xml->talk_speakers->talk_speaker);
        Import::InsertNews($xml->news->newsitem);
    }

    /**
     * @param $rooms Array An array of SimpleXMLElement that holds room data
     */
    private static function InsertRooms($rooms)
    {
        foreach($rooms as $room){
            $id = $room['id'];
            $name = $room['name'];
            if(RoomRepository::getRoomById($id)){
                RoomRepository::updateRoom($id,$name);
            }else{
                RoomRepository::insertRoom($id,$name);
            }
        }
    }

    /**
     * @param $tags Array An array of SimpleXMLElement that holds tag data
     */
    private static function InsertTags($tags)
    {
        foreach($tags as $tag){
            $id = $tag['id'];
            $name = $tag['name'];
            //echo $id.$name;
            if(TagRepository::getTagById($id)){
                TagRepository::updateTag($id,$name);
            }else{
                TagRepository::insertTag($id,$name);
            }
        }
    }

    /**
     * @param $speakers Array An array of SimpleXMLElement that holds speaker data
     */
    private static function InsertSpeakers($speakers)
    {
        foreach($speakers as $speaker){
            $id = $speaker['id'];
            $name = $speaker['name'];
            //echo $id.$name;
            if(SpeakerRepository::getSpeakerById($id)){
                SpeakerRepository::updateSpeaker($id,$name);
            }else{
                SpeakerRepository::insertSpeaker($id,$name);
            }
        }
    }

    /**
     * @param $talks Array An array of SimpleXMLElement that holds talk data
     */
    private static function InsertTalks($talks)
    {
        foreach($talks as $talk){
            $id = $talk['id'];
            $roomId = $talk['room-id'];
            $isKeynote = intval($talk['isKeynote']);
            $title = $talk->title;
            $from = $talk->from;
            $to = $talk->to;
            $content = Import::removeNewLines(Import::GetInnerXml($talk->content));
            if(TalkRepository::getTalkById($id)){
                TalkRepository::updateTalk($id,$roomId,$isKeynote,$title,$from,$to,$content);
            }else{
                TalkRepository::insertTalk($id,$roomId,$isKeynote,$title,$from,$to,$content);
            }
        }
    }

    /**
     * @param $talk_tags Array An array of SimpleXMLElement that holds talk_tag data
     */
    private static function InsertTalkTags($talk_tags)
    {
        foreach($talk_tags as $talk_tag){
            $talkId = $talk_tag['talk-id'];
            $tagId = $talk_tag['tag-id'];
            //echo $talkId.$tagId;
            TalkTagRepository::insertTalkTag($talkId,$tagId);
        }
    }

    /**
     * @param $talk_speakers Array An array of SimpleXMLElement that holds talk_speaker data
     */
    private static function InsertTalkSpeakers($talk_speakers)
    {
        foreach($talk_speakers as $talk_speaker){
            $talkId = $talk_speaker['talk-id'];
            $speakerId = $talk_speaker['speaker-id'];
            //echo $talkId.$tagId;
            TalkSpeakerRepository::insertTalkSpeaker($talkId,$speakerId);
        }
    }

    /**
     * @param $newsitems Array $rooms An array of SimpleXMLElement that holds room data
     */
    private static function InsertNews($newsitems)
    {
        foreach ($newsitems as $newsitem) {
            $importance = $newsitem['importance'];
            $order = $newsitem['order'];
            $title = $newsitem->title;
            $shortDescription = Import::removeNewLines(Import::GetInnerXml($newsitem->short_description));
            $longDescription = Import::removeNewLines(Import::GetInnerXml($newsitem->long_description));
            $image = $newsitem->image;
            //var_dump(array($importance,$order,$title,$shortDescription,$longDescription,$image));
            NewsRepository::insertNewsItem($importance,$order,$title,$shortDescription,$longDescription,$image);
        }

    }

    /**
     * @param $xml \SimpleXMLElement
     * @return string returns the inside of the xml as a string
     */
    private static function GetInnerXml($xml)
    {
        if(empty($xml)){
            return '';
        }
        $innerXML= '';
        foreach (dom_import_simplexml($xml)->childNodes as $child)
        {
            $innerXML .= $child->ownerDocument->saveXML( $child );
        }
        return $innerXML;
    }

    /**
     * @param $string string the string to trim and remove newlines
     * @return string trimmed and newlines removed string
     */
    private static function removeNewLines($string){
        return trim(str_replace("\n","",$string));
    }
} 