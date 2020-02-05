create table usuario (
	id serial primary key,
	nome varchar,
	cpf varchar,
	data_nascimento date,
	senha varchar,

	constraint validar_data check (data_nascimento > '01-01-1900'::date and data_nascimento < current_date),
	constraint validar_nome check (nome ~ '^[^0-9]+$')
);

create or replace function criar_usuario(json jsonb)
returns boolean as
$BODY$
begin
	insert into usuario (nome, cpf, data_nascimento, senha) values (json->>'nome', json->>'cpf', (json->>'dataNascimento')::date, json->>'senha');
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
	update usuario set
	    nome = json->>'nome',
	    cpf = json->>'cpf',
	    data_nascimento = (json->>'dataNascimento')::date,
	    senha = json->>'senha'
	    where id = COALESCE(json->>'id','0')::integer;
	return true;
end;
$BODY$
LANGUAGE plpgsql;

create or replace function remover_usuario(json varchar)
returns boolean as
$BODY$
begin
	if (COALESCE(json, '0')::integer = 0) then
		return false;
	end if;
	delete from usuario where id = COALESCE(json->>'id','0')::integer;
	return true;
end;
$BODY$
LANGUAGE plpgsql;

