CREATE OR REPLACE TRIGGER TG_DESTINO_AUTOMATICO
AFTER UPDATE OF status ON COLETA
FOR EACH ROW
BEGIN
    IF :NEW.status = 'FINALIZADA' THEN
        INSERT INTO DESTINO_FINAL (id_destino, tipo_destino, local_destino, data_recebimento, id_coleta)
        VALUES (
            SEQ_DESTINO.NEXTVAL,
            'Reciclagem',
            'Centro de Reciclagem Municipal',
            SYSDATE,
            :NEW.id_coleta
        );
    END IF;
END;
/

CREATE OR REPLACE TRIGGER TG_ATUALIZA_STATUS_COLETA
AFTER INSERT ON DESTINO_FINAL
FOR EACH ROW
BEGIN
    UPDATE COLETA
       SET status = 'FINALIZADA'
     WHERE id_coleta = :NEW.id_coleta;
END;
/

CREATE OR REPLACE TRIGGER TG_VERIFICA_LICENCA
BEFORE INSERT ON COLETA
FOR EACH ROW
DECLARE
    v_licenca TRANSPORTADOR.licenca_ambiental%TYPE;
BEGIN
    SELECT licenca_ambiental INTO v_licenca
      FROM TRANSPORTADOR
     WHERE id_transportador = :NEW.id_transportador;

    IF v_licenca IS NULL THEN
        RAISE_APPLICATION_ERROR(-20001, 'Transportador sem licença ambiental válida!');
    END IF;
END;
/