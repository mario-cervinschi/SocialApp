package repository;

import domain.Entity;
import domain.validators.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    protected String filename;

    public AbstractFileRepository(Validator<E> validator) {
        super(validator);
    }

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename = fileName;
        loadData();
    }

    protected void loadData(){
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String newLine;
            while((newLine = reader.readLine()) != null){
                System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public abstract E extractEntity(List<String> data);
    public abstract String createEntityAsString(E entity);

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> e = super.delete(id);
        if(e.isEmpty())
            throw new IllegalArgumentException("ID inexistent");
        writeToFile();
        return e;
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.isEmpty())
            writeToFile();
        return e;
    }

    private void writeToFile() {
        try  (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = createEntityAsString(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}