import dev.shared.differences.AssertDifferencesUpdates;
import dev.shared.exception.storage.AttributeTypeExceptionResponseStorage;
import dev.shared.exception.varieties.DataCreateException;
import dev.shared.exception.varieties.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeTypesServiceImpl implements AttributeTypesService {

    private final AttributeTypesRepository repository;
    private final AssertDifferencesUpdates assertDifferencesUpdates;

    @Autowired
    public AttributeTypesServiceImpl(AttributeTypesRepository repository, AssertDifferencesUpdates assertDifferencesUpdates) {
        this.repository = repository;
        this.assertDifferencesUpdates = assertDifferencesUpdates;
    }

    @Override
    public List<AttributeTypes> getAll() {
        return repository.findAll();
    }

    @Override
    public AttributeTypes getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(AttributeTypeExceptionResponseStorage.attributeTypeNotFound(id), HttpStatus.NOT_FOUND));
    }

    @Override
    public AttributeTypes create(AttributeTypes attributeTypes) {
        Boolean isExistsByName = repository.existsByName(attributeTypes.getName());
        if (isExistsByName) {
            throw new DataCreateException(AttributeTypeExceptionResponseStorage.attributeTypeExists(attributeTypes.getName()), HttpStatus.CONFLICT);
        }
        repository.save(attributeTypes);
        return attributeTypes;
    }

    @Override
    public AttributeTypes updateById(Integer id, AttributeTypes dto) {
        AttributeTypes attributeTypes = repository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(AttributeTypeExceptionResponseStorage.attributeTypeNotFound(id), HttpStatus.NOT_FOUND));
        attributeTypes = assertDifferencesUpdates.assertAttributeTypesAndDto(attributeTypes, dto);
        repository.save(attributeTypes);
        return attributeTypes;

    }

    @Override
    public AttributeTypes archiveById(Integer id) {
        AttributeTypes attributeTypes = repository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException(AttributeTypeExceptionResponseStorage.attributeTypeNotFound(id), HttpStatus.NOT_FOUND));
        repository.save(attributeTypes);
        return attributeTypes;
    }
}
