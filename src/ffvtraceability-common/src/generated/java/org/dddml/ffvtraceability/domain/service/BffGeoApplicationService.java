// <autogenerated>
//   This file was generated by dddappp code generator.
//   Any changes made to this file manually will be lost next time the file is regenerated.
// </autogenerated>

package org.dddml.ffvtraceability.domain.service;

import org.dddml.ffvtraceability.domain.*;

public interface BffGeoApplicationService {

    Iterable<BffGeoDto> when(BffGeoServiceCommands.GetAllNorthAmericanStatesAndProvinces c);

    Iterable<BffGeoDto> when(BffGeoServiceCommands.GetCountries c);

    Iterable<BffGeoDto> when(BffGeoServiceCommands.GetStatesAndProvinces c);


}

