package waterElectric.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.waterElectric.entity.WaterElectric;
import com.fu.lhm.waterElectric.validate.WaterElectricValidate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WaterElectricValidateTest {

    @InjectMocks
    private WaterElectricValidate waterElectricValidate;

    @Before
    public void setUp() {
        waterElectricValidate = new WaterElectricValidate();
    }

    @Test
    public void testValidateUpdateWaterElectric_1() {
        Integer integer = null;

        WaterElectric waterElectric = WaterElectric.builder()
                .chiSoDauDien(-1)
                .chiSoDauNuoc(100)
                .chiSoCuoiDien(100)
                .chiSoCuoiNuoc(100)
                .numberElectric(100)
                .numberWater(100)
                .build();

        assertThrows(BadRequestException.class, () -> {
            waterElectricValidate.validateUpdateWaterElectric(waterElectric);
        });
    }

    @Test
    public void testValidateUpdateWaterElectric_2() {
        Integer integer = null;

        WaterElectric waterElectric = WaterElectric.builder()
                .chiSoDauDien(100)
                .chiSoDauNuoc(-1)
                .chiSoCuoiDien(100)
                .chiSoCuoiNuoc(100)
                .numberElectric(100)
                .numberWater(100)
                .build();

        assertThrows(BadRequestException.class, () -> {
            waterElectricValidate.validateUpdateWaterElectric(waterElectric);
        });
    }

    @Test
    public void testValidateUpdateWaterElectric_3() {
        Integer integer = null;

        WaterElectric waterElectric = WaterElectric.builder()
                .chiSoDauDien(200)
                .chiSoDauNuoc(100)
                .chiSoCuoiDien(100)
                .chiSoCuoiNuoc(100)
                .numberElectric(100)
                .numberWater(100)
                .build();

        assertThrows(BadRequestException.class, () -> {
            waterElectricValidate.validateUpdateWaterElectric(waterElectric);
        });
    }

    @Test
    public void testValidateUpdateWaterElectric_4() {
        Integer integer = null;

        WaterElectric waterElectric = WaterElectric.builder()
                .chiSoDauDien(100)
                .chiSoDauNuoc(200)
                .chiSoCuoiDien(100)
                .chiSoCuoiNuoc(100)
                .numberElectric(100)
                .numberWater(100)
                .build();

        assertThrows(BadRequestException.class, () -> {
            waterElectricValidate.validateUpdateWaterElectric(waterElectric);
        });
    }

    @Test
    public void testValidateUpdateWaterElectric_5() {
        Integer integer = null;

        WaterElectric waterElectric = WaterElectric.builder()
                .chiSoDauDien(100)
                .chiSoDauNuoc(100)
                .chiSoCuoiDien(100)
                .chiSoCuoiNuoc(100)
                .numberElectric(-1)
                .numberWater(100)
                .build();

        assertThrows(BadRequestException.class, () -> {
            waterElectricValidate.validateUpdateWaterElectric(waterElectric);
        });
    }

    @Test
    public void testValidateUpdateWaterElectric_6() {
        Integer integer = null;

        WaterElectric waterElectric = WaterElectric.builder()
                .chiSoDauDien(100)
                .chiSoDauNuoc(100)
                .chiSoCuoiDien(100)
                .chiSoCuoiNuoc(100)
                .numberElectric(100)
                .numberWater(-1)
                .build();

        assertThrows(BadRequestException.class, () -> {
            waterElectricValidate.validateUpdateWaterElectric(waterElectric);
        });
    }
}