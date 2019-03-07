alter table systemsetting
  add column if not exists lastupdatedby bigint;

alter table systemsetting
  add column if not exists lastupdated timestamp without time zone;

alter table systemsetting
  add column if not exists created timestamp without time zone;

alter table systemsetting
  drop constraint if exists fk_lastupdateby_userid;

alter table only systemsetting
  add constraint fk_lastupdateby_userid foreign key (lastupdatedby) references userinfo (userinfoid);

alter table systemsetting
  add column if not exists translations jsonb;