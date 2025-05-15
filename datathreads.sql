use railway;
-- Insert into users table (10 users)
INSERT INTO users (user_id, bio, email, image, is_active, nick_name, password, phone, username) VALUES
(1, 'Football lover ⚽', 'conco2444@gmail.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg', 1, 'FootyFan1', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '1234567890', 'user1'),
(2, 'Tech enthusiast', 'user2@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg', 1, 'Techie2', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '0987654321', 'user2'),
(3, 'Messi is GOAT 🐐', 'user3@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg', 1, 'MessiFan3', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '5555555555', 'user3'),
(4, 'Yamal rising star!', 'user4@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png', 1, 'YamalStar4', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '4444444444', 'user4'),
(5, 'Just chilling', 'user5@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg', 1, 'ChillVibes5', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '3333333333', 'user5'),
(6, 'Sports and memes', 'user6@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png', 1, 'MemeLord6', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '2222222222', 'user6'),
(7, 'Photography lover', 'user7@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg', 1, 'ShutterBug7', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '1111111111', 'user7'),
(8, 'Gamer for life 🎮', 'user8@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg', 1, 'GameMaster8', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '6666666666', 'user8'),
(9, 'Fitness freak', 'user9@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg', 1, 'FitLife9', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '7777777777', 'user9'),
(10, 'Foodie at heart', 'user10@example.com', 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png', 1, 'Foodie10', '$2a$12$s8G8haswYErrJ0bknfqIEe/44Y5cszW0.l0xS/aIX09o1euIGIr2S', '8888888888', 'user10');

-- Insert into post table (20 posts)
INSERT INTO post (post_id, comment_count, content, created_at, like_count, repost_count, visibility, user_id) VALUES
(1, 5, 'What a game last night! Messi with another banger! 🐐', '2025-05-10 10:00:00', 15, 3, 'PUBLIC', 1),
(2, 3, 'Yamal is the future of football! 🔥', '2025-05-11 12:30:00', 10, 2, 'PUBLIC', 4),
(3, 2, 'Just built a new PC, loving the performance! 💻', '2025-05-12 15:45:00', 8, 1, 'PUBLIC', 2),
(4, 4, 'Can’t believe Messi is still this good at 38! 😍', '2025-05-12 18:20:00', 20, 4, 'PUBLIC', 3),
(5, 1, 'Chilling with some music and coffee ☕', '2025-05-13 09:10:00', 5, 0, 'PUBLIC', 5),
(6, 6, 'This meme about football is too good! 😂', '2025-05-13 14:25:00', 12, 3, 'PUBLIC', 6),
(7, 0, 'Captured a stunning sunset today! 📸', '2025-05-14 17:50:00', 7, 1, 'PUBLIC', 7),
(8, 2, 'Just hit a new PR in the gym! 💪', '2025-05-14 20:15:00', 9, 2, 'PUBLIC', 9),
(9, 3, 'Who’s excited for the new FIFA game? 🎮', '2025-05-15 08:00:00', 11, 1, 'PUBLIC', 8),
(10, 4, 'Tried a new sushi place, amazing! 🍣', '2025-05-15 10:20:00', 6, 0, 'PUBLIC', 10),
(11, 2, 'Messi’s free kick was unreal! 🥅', '2025-05-10 11:00:00', 14, 2, 'PUBLIC', 1),
(12, 1, 'Tech trends for 2025 are wild! 🚀', '2025-05-11 14:00:00', 7, 1, 'PUBLIC', 2),
(13, 3, 'Yamal’s dribbling is next level! 🌟', '2025-05-12 16:30:00', 13, 3, 'PUBLIC', 4),
(14, 0, 'Lazy Sunday vibes 😎', '2025-05-13 10:45:00', 4, 0, 'PUBLIC', 5),
(15, 5, 'This meme got me crying! 😭', '2025-05-13 19:00:00', 10, 2, 'PUBLIC', 6),
(16, 2, 'Photography tips anyone? 📷', '2025-05-14 12:00:00', 6, 1, 'PUBLIC', 7),
(17, 1, 'Gym grind never stops! 🏋️', '2025-05-14 21:30:00', 8, 1, 'PUBLIC', 9),
(18, 3, 'Gaming all night, who’s with me? 🎮', '2025-05-15 01:00:00', 9, 2, 'PUBLIC', 8),
(19, 2, 'Best burger in town, hands down! 🍔', '2025-05-15 09:30:00', 5, 0, 'PUBLIC', 10),
(20, 4, 'Messi or Ronaldo? Let’s settle this! ⚽', '2025-05-15 10:00:00', 18, 5, 'PUBLIC', 3);

-- Insert into post_media table (15 media entries)
INSERT INTO post_media (post_id, media_url) VALUES
(1, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(2, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png'),
(4, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(6, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg'),
(7, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg'),
(9, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg'),
(10, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg'),
(11, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(13, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png'),
(15, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg'),
(16, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg'),
(18, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg'),
(19, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg'),
(20, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(6, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png');

-- Insert into comment table (30 comments, including replies)
INSERT INTO comment (comment_id, content, create_at, visibility, post_id, user_id, parent_id, like_count) VALUES
(1, 'Totally agree, Messi is unreal! 🐐', '2025-05-10 10:15:00', 'PUBLIC', 1, 3, NULL, 5),
(2, 'That goal was insane! 🔥', '2025-05-10 10:20:00', 'PUBLIC', 1, 4, NULL, 3),
(3, 'Messi forever! 😍', '2025-05-10 10:25:00', 'PUBLIC', 1, 6, 1, 2),
(4, 'Yamal’s potential is crazy! 🌟', '2025-05-11 12:40:00', 'PUBLIC', 2, 1, NULL, 4),
(5, 'Future Ballon d’Or winner!', '2025-05-11 12:45:00', 'PUBLIC', 2, 3, NULL, 2),
(6, 'Nice build! What specs?', '2025-05-12 16:00:00', 'PUBLIC', 3, 8, NULL, 1),
(7, 'Messi’s still got it! 🥅', '2025-05-12 18:30:00', 'PUBLIC', 4, 1, NULL, 6),
(8, 'GOAT for a reason!', '2025-05-12 18:35:00', 'PUBLIC', 4, 4, NULL, 3),
(9, 'Love the vibe! What music?', '2025-05-13 09:20:00', 'PUBLIC', 5, 7, NULL, 1),
(10, 'This meme is gold! 😂', '2025-05-13 14:30:00', 'PUBLIC', 6, 2, NULL, 4),
(11, 'Share it, bro! 😄', '2025-05-13 14:35:00', 'PUBLIC', 6, 8, 10, 2),
(12, 'Stunning shot! What camera?', '2025-05-14 18:00:00', 'PUBLIC', 7, 5, NULL, 1),
(13, 'Beast mode! What’s the PR?', '2025-05-14 20:20:00', 'PUBLIC', 8, 6, NULL, 2),
(14, 'FIFA hype is real! 🙌', '2025-05-15 08:10:00', 'PUBLIC', 9, 1, NULL, 3),
(15, 'That sushi looks fire! 🍣', '2025-05-15 10:25:00', 'PUBLIC', 10, 9, NULL, 1),
(16, 'Free kick master! 🐐', '2025-05-10 11:10:00', 'PUBLIC', 11, 3, NULL, 4),
(17, 'Mind blown! 🤯', '2025-05-11 14:10:00', 'PUBLIC', 12, 7, NULL, 1),
(18, 'Yamal’s dribbles are silky! 😎', '2025-05-12 16:40:00', 'PUBLIC', 13, 6, NULL, 3),
(19, 'Chill vibes only! 😍', '2025-05-13 10:50:00', 'PUBLIC', 14, 10, NULL, 1),
(20, 'This meme is too much! 😂', '2025-05-13 19:10:00', 'PUBLIC', 15, 8, NULL, 2),
(21, 'Use manual focus for sharper shots!', '2025-05-14 12:10:00', 'PUBLIC', 16, 2, NULL, 1),
(22, 'Keep grinding! 💪', '2025-05-14 21:40:00', 'PUBLIC', 17, 4, NULL, 1),
(23, 'Count me in! 🎮', '2025-05-15 01:10:00', 'PUBLIC', 18, 6, NULL, 2),
(24, 'That burger looks juicy! 🍔', '2025-05-15 09:40:00', 'PUBLIC', 19, 5, NULL, 1),
(25, 'Messi, no debate! 🐐', '2025-05-15 10:05:00', 'PUBLIC', 20, 1, NULL, 5),
(26, 'Ronaldo’s got more goals tho! 😏', '2025-05-15 10:10:00', 'PUBLIC', 20, 4, NULL, 3),
(27, 'Messi’s playmaking > Ronaldo’s finishing', '2025-05-15 10:15:00', 'PUBLIC', 20, 6, 25, 2),
(28, 'Yamal’s gonna surpass both! 🌟', '2025-05-15 10:20:00', 'PUBLIC', 20, 8, NULL, 1),
(29, 'That goal was a screamer! ⚽', '2025-05-10 10:30:00', 'PUBLIC', 1, 9, NULL, 2),
(30, 'Haha, same! 😄', '2025-05-13 14:40:00', 'PUBLIC', 6, 10, 10, 1);

-- Insert into comment_media table (10 media entries)
INSERT INTO comment_media (comment_id, media_url) VALUES
(1, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(4, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png'),
(7, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(10, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg'),
(12, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257961/threads_app/qtjzi35yzi2tky7utgld.jpg'),
(16, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(18, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png'),
(20, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746257909/threads_app/fqye0stnl4asb1zp8l4d.jpg'),
(25, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094250/messi_rqebnm.jpg'),
(28, 'https://res.cloudinary.com/dmcg6uu1f/image/upload/v1746094259/yamal_pindwz.png');

-- Insert into post_like table (40 likes)
INSERT INTO post_like (id, post_id, user_id, comment_id) VALUES
(1, 1, 3, NULL), (2, 1, 4, NULL), (3, 1, 6, NULL), (4, 1, 9, NULL), (5, 1, 10, NULL),
(6, 2, 1, NULL), (7, 2, 3, NULL), (8, 2, 6, NULL), (9, 3, 8, NULL), (10, 3, 7, NULL),
(11, 4, 1, NULL), (12, 4, 4, NULL), (13, 4, 6, NULL), (14, 4, 9, NULL), (15, 5, 7, NULL),
(16, 6, 2, NULL), (17, 6, 8, NULL), (18, 6, 10, NULL), (19, 7, 5, NULL), (20, 7, 2, NULL),
(21, 8, 6, NULL), (22, 8, 4, NULL), (23, 9, 1, NULL), (24, 9, 6, NULL), (25, 10, 9, NULL),
(26, NULL, 3, 1), (27, NULL, 4, 1), (28, NULL, 6, 3), (29, NULL, 1, 4), (30, NULL, 3, 4),
(31, 11, 3, NULL), (32, 13, 6, NULL), (33, 15, 8, NULL), (34, 20, 1, NULL), (35, 20, 4, NULL),
(36, 20, 6, NULL), (37, 20, 8, NULL), (38, NULL, 1, 25), (39, NULL, 3, 25), (40, NULL, 4, 26);

-- Insert into follow table (30 follow relationships)
INSERT INTO follow (follow_id, follower_id, following_id) VALUES
(1, 1, 3), (2, 1, 4), (3, 1, 6), (4, 2, 7), (5, 2, 8), (6, 3, 1), (7, 3, 4), (8, 3, 6),
(9, 4, 1), (10, 4, 3), (11, 4, 6), (12, 5, 7), (13, 5, 10), (14, 6, 1), (15, 6, 3),
(16, 6, 4), (17, 7, 2), (18, 7, 5), (19, 8, 2), (20, 8, 6), (21, 9, 4), (22, 9, 6),
(23, 10, 5), (24, 10, 9), (25, 1, 9), (26, 3, 9), (27, 4, 9), (28, 6, 9), (29, 8, 9), (30, 2, 9);

-- Insert into reposts table (15 reposts)
INSERT INTO reposts (id, reposted_at, post_id, user_id) VALUES
(1, '2025-05-10 11:00:00', 1, 3), (2, '2025-05-10 11:05:00', 1, 6), (3, '2025-05-11 13:00:00', 2, 1),
(4, '2025-05-12 16:15:00', 3, 8), (5, '2025-05-12 19:00:00', 4, 6), (6, '2025-05-13 15:00:00', 6, 2),
(7, '2025-05-14 18:30:00', 7, 5), (8, '2025-05-14 20:30:00', 8, 4), (9, '2025-05-15 08:30:00', 9, 6),
(10, '2025-05-10 11:30:00', 11, 4), (11, '2025-05-12 17:00:00', 13, 3), (12, '2025-05-13 19:30:00', 15, 8),
(13, '2025-05-15 10:30:00', 20, 6), (14, '2025-05-15 10:35:00', 20, 1), (15, '2025-05-15 10:40:00', 20, 4);

-- Insert into saved_posts table (15 saved posts)
INSERT INTO saved_posts (id, post_id, user_id) VALUES
(1, 1, 3), (2, 1, 4), (3, 2, 1), (4, 4, 6), (5, 6, 2), (6, 7, 5), (7, 8, 4), (8, 9, 1),
(9, 10, 9), (10, 11, 3), (11, 13, 6), (12, 15, 8), (13, 20, 1), (14, 20, 4), (15, 20, 6);

-- Insert into notifications table (20 notifications)
INSERT INTO notifications (id, created_at, is_read, post_id, type, receiver_id, sender_id) VALUES
(1, '2025-05-10 10:15:00', 0, 1, 'comment', 1, 3), (2, '2025-05-10 10:20:00', 0, 1, 'comment', 1, 4),
(3, '2025-05-10 10:25:00', 0, 1, 'comment', 3, 6), (4, '2025-05-11 12:40:00', 0, 2, 'comment', 4, 1),
(5, '2025-05-12 16:00:00', 0, 3, 'comment', 2, 8), (6, '2025-05-12 18:30:00', 0, 4, 'comment', 3, 1),
(7, '2025-05-13 14:30:00', 0, 6, 'comment', 6, 2), (8, '2025-05-14 20:20:00', 0, 8, 'comment', 9, 6),
(9, '2025-05-15 08:10:00', 0, 9, 'comment', 8, 1), (10, '2025-05-15 10:05:00', 0, 20, 'comment', 3, 1),
(11, '2025-05-10 10:00:00', 0, 1, 'like', 1, 3), (12, '2025-05-11 12:30:00', 0, 2, 'like', 4, 1),
(13, '2025-05-12 18:20:00', 0, 4, 'like', 3, 6), (14, '2025-05-13 14:25:00', 0, 6, 'like', 6, 2),
(15, '2025-05-15 10:00:00', 0, 20, 'like', 3, 4), (16, '2025-05-10 10:00:00', 0, NULL, 'follow', 3, 1),
(17, '2025-05-11 12:00:00', 0, NULL, 'follow', 4, 3), (18, '2025-05-12 15:00:00', 0, NULL, 'follow', 6, 4),
(19, '2025-05-14 20:00:00', 0, NULL, 'follow', 9, 6), (20, '2025-05-15 08:00:00', 0, NULL, 'follow', 8, 2);