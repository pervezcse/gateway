drop table if exists device CASCADE;
drop table if exists gateway CASCADE;
drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start with 4 increment by 1;
create table gateway (id varchar(255) not null, ipv4_address varchar(255), name varchar(255), primary key (id));
create table device (id bigint not null, vendor varchar(255), gateway_id varchar(255) not null, status varchar(255), creation_date_time timestamp, primary key (id));
alter table device add constraint fk_device_gateway_id foreign key (gateway_id) references gateway;

INSERT INTO gateway (id, name, ipv4_address) VALUES
('8dd5f315-9788-4d00-87bb-10eed9eff501', 'Gateway 1', '192.168.1.1'),
('8dd5f315-9788-4d00-87bb-10eed9eff502', 'Gateway 2', '192.168.1.2'),
('8dd5f315-9788-4d00-87bb-10eed9eff503', 'Gateway 3', '192.168.1.3');

INSERT INTO device (id, vendor, gateway_id, status, creation_date_time) VALUES
(1, 'vendor 1', '8dd5f315-9788-4d00-87bb-10eed9eff501', 'ONLINE', CURRENT_TIMESTAMP),
(2, 'vendor 2', '8dd5f315-9788-4d00-87bb-10eed9eff501', 'ONLINE', CURRENT_TIMESTAMP),
(3, 'vendor 3', '8dd5f315-9788-4d00-87bb-10eed9eff502', 'ONLINE', CURRENT_TIMESTAMP);
