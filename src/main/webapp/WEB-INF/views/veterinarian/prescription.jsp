<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>VetCare Pro - EMR</title>

        <link rel="stylesheet" href="css/DashboardVeteStyle.css">
        <link href="css/sidebar.css" rel="stylesheet">
        <link href="css/headerVeteStyle.css" rel="stylesheet">
        <link href="css/MedicalRecordStyle.css" rel="stylesheet">
        <link href="css/PrepscriptionStyle.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    </head>

    <body>
        <div class="layout">

            <jsp:include page="sidebar.jsp"/>
            <main class="main">
                <jsp:include page="header.jsp"/>

                <div class="emr-container">

                    <div class="back-link">
                        <a href="assignedCases">
                            <i class="fa fa-arrow-left"></i> Back to List
                        </a>
                    </div>



                    <div class="emr-card">

                        <form action="preController" method="post">

                            <input type="hidden" name="medicalRecordID"
                                   value="${medicalRecordID}"/>

                            <!-- SEARCH -->

                           
                            <div class="emr-info">

                                <!-- PATIENT -->
                                <div class="info-box">
                                    <h4>PATIENT DETAILS</h4>
                                    <div class="info-grid">
                                        <div>
                                            <label>Name</label>
                                            <p>${emr.catName}</p>
                                        </div>
                                        <div>
                                            <label>Species</label>
                                            <p>Cat</p>
                                        </div>
                                        <div>
                                            <label>Breed</label>
                                            <p>${emr.breed}</p>
                                        </div>
                                        <div>
                                            <label>Age</label>
                                            <p>${emr.age} years</p>
                                        </div>
                                        <div>
                                            <label>Booking Note</label>
                                            <p>${emr.note}</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- OWNER -->
                                <div class="info-box">
                                    <h4>OWNER DETAILS</h4>
                                    <div class="info-grid">
                                        <div>
                                            <label>Owner Name</label>
                                            <p>${emr.ownerName}</p>
                                        </div>
                                        <div>
                                            <label>Contact Phone</label>
                                            <p>${emr.phone}</p>
                                        </div>
                                    </div>
                                </div>

                            </div>



                            <hr class="divider"/>
                            <div class="step-nav">

                                <a href="EmrController?medicalRecordID=${emr.medicalRecordID}"
                                   class="step-item ${activeStep == 1 ? 'active' : ''}">
                                    <span class="step-number">1</span>
                                    <span class="step-label">EXAM</span>
                                </a>



                                <a href="xray?medicalRecordID=${emr.medicalRecordID}"
                                   class="step-item ${activeStep == 2 ? 'active' : ''}">
                                    <span class="step-number">2</span>
                                    <span class="step-label">X-RAY</span>
                                </a>

                                <a href="bloodtest?medicalRecordID=${emr.medicalRecordID}"
                                   class="step-item ${activeStep == 3 ? 'active' : ''}">
                                    <span class="step-number">3</span>
                                    <span class="step-label">BLOOD TEST</span>
                                </a>

                                <a href="preController?medicalRecordID=${emr.medicalRecordID}"
                                   class="step-item ${activeStep == 4 ? 'active' : ''}">
                                    <span class="step-number">4</span>
                                    <span class="step-label">PRESCRIPTION</span>
                                </a>
                            </div>
                                     <c:if test="${status ne 'Completed'}">

                                <div class="form-group">
                                    <label>Search & Add Medicine</label>
                                    <input type="text"
                                           id="drugSearch"
                                           placeholder="Search medicine name..."
                                           autocomplete="off">
                                    <div id="searchResult"></div>
                                </div>
                            </c:if>
                            <!-- PRESCRIPTION ITEMS -->
                            <div class="form-group">
                                <label>Prescription Items</label>
                                <div id="prescriptionContainer">
                                    <div id="emptyBox">
                                        No medicines added yet.
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Follow-up Date</label>
                                <input type="date" name="followUpDate" class="input-control" value="${followUpdate}">
                            </div>

                            <div class="form-group">
                                <label>Additional Advice</label>
                                <input type="text" name="advice" class="input-control" value="${advice}">
                            </div>
                            <c:if test="${status ne 'Completed'}">

                                <button type="submit"
                                        name="action"
                                        value="save"
                                        class="submit-btn">
                                    <i class="fa fa-file-medical"></i> Save Prescription
                                </button>

                                <button type="submit"
                                        name="action"
                                        value="complete"
                                        class="submit-btn">
                                    <i class="fa fa-file-medical"></i> Completed Medical Record
                                </button>

                            </c:if>

                        </form>
                        <div class="floating-nav">

                            <!-- Previous -->
                            <a href="EmrController?medicalRecordID=${emr.medicalRecordID}"
                               class="btn-floating btn-prev">
                                <i class="fa fa-arrow-left"></i> Previous
                            </a>

                        </div>
                    </div>
                </div>
            </main>
        </div>

        <!-- LOAD DRUG LIST -->
        <script>
            const drugs = [];
            <c:forEach var="d" items="${drugList}">
            drugs.push({
                id: "<c:out value='${d.drugID}'/>",
                name: "<c:out value='${d.name}'/>"
            });
            </c:forEach>
        </script>

        <script>
            const input = document.getElementById("drugSearch");
            const resultBox = document.getElementById("searchResult");
            const container = document.getElementById("prescriptionContainer");

            let addedDrugs = [];

            input.addEventListener("keyup", function () {

                const keyword = this.value.trim().toLowerCase();
                resultBox.innerHTML = "";

                if (keyword === "") {
                    resultBox.style.display = "none";
                    return;
                }

                const filtered = drugs
                        .filter(d =>
                            d.name.toLowerCase().includes(keyword) &&
                                    !addedDrugs.includes(d.id)
                        )
                        .slice(0, 5);

                if (filtered.length === 0) {
                    resultBox.style.display = "none";
                    return;
                }

                filtered.forEach(d => {

                    const div = document.createElement("div");
                    div.className = "search-item";

                    const nameSpan = document.createElement("span");
                    nameSpan.textContent = d.name;

                    const btn = document.createElement("button");
                    btn.type = "button";
                    btn.className = "add-btn";
                    btn.innerHTML = '<i class="fa fa-plus"></i>';
                    btn.onclick = () => addDrug(d.id, d.name);

                    div.appendChild(nameSpan);
                    div.appendChild(btn);
                    resultBox.appendChild(div);
                });

                resultBox.style.display = "block";
            });

            function getDrugName(id) {
                const drug = drugs.find(d => d.id === id);
                return drug ? drug.name : "";
            }

            function addDrug(id, name, quantityValue = "", instructionValue = "") {

                if (addedDrugs.includes(id))
                    return;

                const emptyBox = document.getElementById("emptyBox");
                if (emptyBox)
                    emptyBox.remove();

                addedDrugs.push(id);

                const card = document.createElement("div");
                card.id = "drug_" + id;
                card.className = "drug-card";

                const title = document.createElement("div");
                title.className = "drug-title";
                title.textContent = name;

                const hiddenInput = document.createElement("input");
                hiddenInput.type = "hidden";
                hiddenInput.name = "drugID";
                hiddenInput.value = id;

                const quantityDiv = document.createElement("div");
                quantityDiv.className = "drug-field";

                const quantityLabel = document.createElement("label");
                quantityLabel.textContent = "Quantity";

                const quantityInput = document.createElement("input");
                quantityInput.type = "number";
                quantityInput.name = "quantity_" + id;
                quantityInput.min = "1";
                quantityInput.required = true;
                quantityInput.value = quantityValue;

                quantityDiv.appendChild(quantityLabel);
                quantityDiv.appendChild(quantityInput);

                const instructionDiv = document.createElement("div");
                instructionDiv.className = "drug-field";

                const instructionLabel = document.createElement("label");
                instructionLabel.textContent = "Instructions";

                const instructionInput = document.createElement("input");
                instructionInput.type = "text";
                instructionInput.name = "instruction_" + id;
                instructionInput.required = true;
                instructionInput.value = instructionValue;

                instructionDiv.appendChild(instructionLabel);
                instructionDiv.appendChild(instructionInput);

                const removeBtn = document.createElement("button");
                removeBtn.type = "button";
                removeBtn.className = "remove-btn";
                removeBtn.innerHTML = '<i class="fa fa-trash"></i> Remove';
                removeBtn.onclick = () => removeDrug(id);

                const contentRow = document.createElement("div");
                contentRow.className = "drug-content";

                contentRow.appendChild(quantityDiv);
                contentRow.appendChild(instructionDiv);
                contentRow.appendChild(removeBtn);

                card.appendChild(title);
                card.appendChild(hiddenInput);
                card.appendChild(contentRow);

                container.appendChild(card);
            }

            function removeDrug(id) {
                const card = document.getElementById("drug_" + id);
                if (card)
                    card.remove();

                addedDrugs = addedDrugs.filter(d => d !== id);

                if (container.children.length === 0) {
                    container.innerHTML =
                            '<div id="emptyBox">No medicines added yet.</div>';
                }
            }
        </script>

        <!-- AUTO LOAD IF HAS PRESCRIPTION -->
        <c:if test="${not empty prescriptionList}">
            <script>
                window.addEventListener("DOMContentLoaded", function () {
                <c:forEach var="p" items="${prescriptionList}">
                    addDrug(
                            "${p.drugID}",
                            getDrugName("${p.drugID}"),
                            "${p.quantity}",
                            "${p.instruction}"
                            );
                </c:forEach>
                });
            </script>
        </c:if>

    </body>
</html>