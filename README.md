# Data Sources
## 從哪個API來的？
twitter

## 內容主題為何？
iphone

## 下哪個查詢字串？
iphone


# Data Format

## JSON的欄位架構
  {
    created_at
    id
    id_str
    text
    source
    truncated
    in_reply_to_status_id
    in_reply_to_status_id_str
    in_reply_to_user_id
    in_reply_to_user_id_str
    in_reply_to_screen_name
    user
    {
      id
      id_str
      name
      screen_name
      location
      url
      description
      protected
      verified
      followers_count
      friends_count
      listed_count
      favourites_count
      statuses_count
      created_at
      utc_offset
      time_zone
      geo_enabled
      lang
      contributors_enabled
      is_translator
      profile_background_color
      profile_background_image_url
      profile_background_image_url_https
      profile_background_tile
      profile_link_color
      profile_sidebar_border_color
      profile_sidebar_fill_color
      profile_text_color
      profile_use_background_image
      profile_image_url
      profile_image_url_https
      default_profile
      default_profile_image
      following
      follow_request_sent
      notifications
    }
    geo
    coordinates
    place
    contributors
    is_quote_status
    retweet_count
    favorite_count
    entities
    {
      hashtags
      urls
      user_mentions
      symbols
    }
    favorited
    retweeted
    possibly_sensitive
    filter_level
    lang
    timestamp_ms
    version
    timestamp
  }

## CSV的Fields
  fields => [created_at,id,id_str,text,source,truncated,in_reply_to_status_id,in_reply_to_status_id_str,in_reply_to_user_id,in_reply_to_user_id_str,in_reply_to_screen_name,"[user][id]","[user][id_str]","[user][name]","[user][screen_name]","[user][location]","[user][url]","[user][description]","[user][protected]","[user][verified]","[user][followers_count]","[user][friends_count]","[user][listed_count]","[user][favourites_count]","[user][statuses_count]","[user][created_at]","[user][utc_offset]","[user][time_zone]","[user][geo_enabled]","[user][lang]","[user][contributors_enabled]","[user][is_translator]","[user][profile_background_color]","[user][profile_background_image_url]","[user][profile_background_image_url_https]","[user][profile_background_tile]","[user][profile_link_color]","[user][profile_sidebar_border_color]","[user][profile_sidebar_fill_color]","[user][profile_text_color]","[user][profile_use_background_image]","[user][profile_image_url]","[user][profile_image_url_https]","[user][profile_banner_url]","[user][default_profile]","[user][default_profile_image]","[user][following]","[user][follow_request_sent]","[user][notifications]",geo,coordinates,place,contributors,is_quote_status,retweet_count,favorite_count,favorited,retweeted,possibly_sensitive,filter_level,lang,timestamp_ms,version,timestamp]