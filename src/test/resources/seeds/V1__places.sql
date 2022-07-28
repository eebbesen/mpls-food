INSERT
  INTO place(id, address, date_created, last_updated, name, website, app, order_ahead)
VALUES (1,
        '121 S 8th Street #235
Minneapolis, MN 55402',
        NOW(),
        NOW(),
        'Ginelli''s Pizza',
        'https://www.ginellispizza.com/',
        false,
        true);

INSERT
  INTO place(id, address, date_created, last_updated, name, website, app, order_ahead)
VALUES (2,
        '607 Marquette Ave.
Minneapolis, MN 55402',
        NOW(),
        NOW(),
        'Taco John''s',
        'https://locations.tacojohns.com/mn/minneapolis/607-marquette-ave/',
        true,
        false);