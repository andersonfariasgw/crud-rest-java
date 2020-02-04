create table usuario (
	id serial primary key,
	nome varchar,
	cpf varchar
);

create or replace function criar_usuario(json jsonb)
returns boolean as
$BODY$
begin
	insert into usuario (nome, cpf) values (json->>'nome', json->>'cpf');
	return true;
end;
$BODY$
LANGUAGE plpgsql;


create or replace function alterar_usuario(json jsonb)
returns boolean as
$BODY$
begin
	if (COALESCE(json->>'id', '0')::integer = 0) then
		return false;
	end if;
	update usuario set nome = json->>'nome', cpf = json->>'cpf' where id = COALESCE(json->>'id','0')::integer;
	return true;
end;
$BODY$
LANGUAGE plpgsql;


create or replace function remover_usuario(json jsonb)
returns boolean as
$BODY$
begin
	if (COALESCE(json->>'id', '0')::integer = 0) then
		return false;
	end if;
	delete from usuario where id = COALESCE(json->>'id','0')::integer;
	return true;
end;
$BODY$
LANGUAGE plpgsql;

